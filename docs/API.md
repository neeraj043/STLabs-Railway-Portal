# API reference

Base URL: `http://localhost:8080`. All API endpoints are under `/api`; JSON is used for request and response bodies. Interactive OpenAPI docs are available at [`/swagger-ui.html`](http://localhost:8080/swagger-ui.html) when the backend is running.

## Health

### `GET /api/health`

Example response:

```json
{ "status": "UP" }
```

## Authentication

The portal uses a server-side session cookie. Sign-up and login establish the session; send the cookie on subsequent requests. Mutating requests require the `X-XSRF-TOKEN` header matching the `XSRF-TOKEN` cookie. Fetch `GET /api/auth/csrf` first to obtain the CSRF cookie/token.

### `GET /api/auth/csrf`

Returns a CSRF token as text and sets the `XSRF-TOKEN` cookie.

### `POST /api/auth/signup`

Creates an account and signs it in. Passwords must be 8–72 characters.

```json
{ "name": "Aanya Sharma", "email": "aanya@example.com", "password": "choose-a-password" }
```

Returns `201 Created` with the account's `id`, `name`, and `email` (never the password). An existing email returns `409 Conflict`.

### `POST /api/auth/login`

```json
{ "email": "aanya@example.com", "password": "choose-a-password" }
```

Returns the account and sets an authenticated session. Invalid credentials return `401 Unauthorized`.

### `GET /api/auth/me`

Returns the current session's account. An unauthenticated request returns `401 Unauthorized`.

### `POST /api/auth/logout`

Invalidates the current session and returns `204 No Content`.

## Trains

### `GET /api/trains?date=YYYY-MM-DD`

Returns the daily train schedule and the seat availability for the requested travel date. If omitted, `date` defaults to today. The 12 timetable stations have at least one direct train for every distinct ordered origin/destination pair (132 combinations; same-station trips are excluded). All sample trains run daily, and startup seeds date-specific availability for the next 30 calendar days. Availability is independent per train and travel date.

Requires an authenticated session.

```json
[
  {
    "id": 1,
    "code": "ST101",
    "name": "Deccan Star",
    "origin": "Mumbai Central",
    "destination": "Pune Junction",
    "departureTime": "06:15:00",
    "arrivalTime": "09:40:00",
    "totalSeats": 120,
    "availableSeats": 84,
    "fare": 450.00,
    "duration": "3h 25m",
    "serviceFrequency": "DAILY"
  }
]
```

## Bookings

### `GET /api/bookings`

Returns the bookings saved in the database. Requires an authenticated session. A booking has outbound `trainId`, `trainCode`, `trainName`, `origin`, `destination`, optional return train fields (`returnTrainId`, `returnTrainCode`, `returnTrainName`, `returnOrigin`, `returnDestination`, `returnDate`), `passengerName`, `passengers`, `email`, `seats`, optional `mealPreference` (`VEG`, `NON_VEG`, or `JAIN`), `totalFare`, `status`, optional `refundMethod`, `travelDate`, and `createdAt`. Insured bookings also include `insurancePolicyNumber`, `insurancePremium`, and `insuranceCoverPerPerson`. Insurance application personal data is stored for the lab but is not returned in booking responses.

### `POST /api/bookings`

Creates a booking; successful requests return `201 Created`.

Request:

```json
{
  "trainId": 1,
  "passengerName": "Aanya Sharma",
  "passengerNames": ["Aanya Sharma", "Ravi Sharma"],
  "email": "aanya@example.com",
  "seats": 2,
  "travelDate": "2026-11-20",
  "mealPreference": "JAIN",
  "returnTrainId": 11,
  "returnDate": "2026-11-24",
  "travelInsurance": {
    "insuredName": "Aanya Sharma",
    "insuredDateOfBirth": "1990-04-20",
    "insuredGender": "FEMALE",
    "insuredPhone": "9876543210",
    "insuredEmail": "aanya@example.com",
    "insuredAddress": "12 Sample Road, Pune",
    "nomineeName": "Ravi Sharma",
    "nomineeRelationship": "SPOUSE",
    "nomineeDateOfBirth": "1988-08-12",
    "nomineePhone": "9876543211"
  }
}
```

`trainId`, `passengerName`, `email`, and `travelDate` are required. The email must be syntactically valid. `passengerNames` is an optional list of 1–6 passenger names; when provided, its length determines the reserved seat count. Legacy clients may omit it and send `seats` instead (1–6). For a round trip, provide both `returnTrainId` and `returnDate`; the selected return train must run the reverse route. The fare covers every passenger on both legs. Seat availability is tracked independently for the outbound and return travel dates.

`travelInsurance` is optional. When supplied, each application field is required and validated: the insured name must match the lead passenger; birth dates must be in the past; mobile numbers must be valid 10-digit Indian numbers; email, address length, gender, and nominee relationship are checked; the nominee must differ from the insured passenger. It issues one policy number per booking, covering up to ₹2,00,000 accidental cover per passenger for ₹1 per booked passenger. The premium is included in `totalFare` and returned as `insurancePremium`. The policy number appears in the successful booking response. This is a classroom simulation only, not a real insurance product or contract.

`mealPreference` is optional. Allowed values are `VEG`, `NON_VEG`, and `JAIN`; the selected preference applies to all passengers on the booking and is included in the booking response and confirmation. No meal fee is added in this training application. Other values are rejected by validation.

### `POST /api/bookings/{id}/cancel`

Cancels a confirmed booking, restores seats for each journey leg, records the chosen dummy refund method, and returns the updated booking. The portal hides cancelled bookings from the active “My trips” list; a cancelled booking remains available by ID so its status and refund selection can be checked.

Request:

```json
{ "refundMethod": "ORIGINAL_ACCOUNT" }
```

Allowed `refundMethod` values are `ORIGINAL_ACCOUNT`, `DEBIT_CARD`, and `CREDIT_CARD`. These are training-only choices: the portal does not collect card or bank details and does not issue real refunds. Cancelling an already-cancelled booking returns `409 Conflict`.

### `GET /api/bookings/{id}`

Looks up one booking by its unique booking ID and returns its full booking details. A missing ID returns `404 Not Found`. Requires an authenticated session.

## Destination hotels

The hotel catalog is synthetic. The portal limits the selectable cities to destination cities represented by the user's train bookings. The API catalog contains sample listings for destinations in the timetable.

### `GET /api/hotels?city=Pune`

Returns the sample hotel listings for a supported destination city, including `id`, `name`, `city`, `address`, `nightlyRate`, `rating`, and `description`. Requires an authenticated session.

### `POST /api/hotel-bookings`

Creates a separate hotel reservation. Check-in cannot be in the past, check-out must follow check-in, guests must be 1–6, and the hotel ID, lead guest name, and email are required and validated. The sample price is the nightly rate multiplied by nights; no real payment is made. Returns `201 Created` with a unique `confirmationNumber`.

```json
{
  "hotelId": "pune-hotel-1",
  "checkIn": "2026-11-20",
  "checkOut": "2026-11-22",
  "guests": 2,
  "guestName": "Aanya Sharma",
  "email": "aanya@example.com"
}
```

### `GET /api/hotel-bookings`

Returns the signed-in user's saved sample hotel reservations, including hotel, city, dates, guests, total price, status, and confirmation number.

## Error behavior

Validation errors and unavailable resources are useful student test targets. Confirm the observed HTTP status and body against the expected behavior in your exercise; error responses are not yet standardized.
