# Instructor answer key — 40 original findings

**Do not distribute this file with the student handout.** The app is designed as a practice target, not production software. Defect numbers match [`STUDENT_EXERCISES.md`](STUDENT_EXERCISES.md).

| # | Area | Seeded defect / expected observation | Primary location |
|---|---|---|---|
| 1 | UI | Dashboard date is fixed text and drifts from the actual date. | `frontend/src/App.jsx`, welcome eyebrow |
| 2 | UI | **Resolved:** default dates now use the browser’s local calendar day. | `frontend/src/App.jsx`, `localDateString` |
| 3 | Search | Origin/destination matching is case-sensitive and exact. | `frontend/src/App.jsx`, `filtered` |
| 4 | Search | Same origin and destination is accepted; no route-level validation is performed. | `frontend/src/App.jsx`, search fields |
| 5 | Search | Changing one endpoint leaves an incompatible selected endpoint in place. | `frontend/src/App.jsx`, station state handlers |
| 6 | UI | **Resolved:** the trip-type control switches between one-way and round-trip searches. | `frontend/src/App.jsx`, `.trip-type-toggle` |
| 7 | UI | Search shortcut is shown but no keyboard handler is registered. | `frontend/src/App.jsx`, `.quick-search` |
| 8 | Schedule | **Resolved:** search requests date-specific availability from the API. | `frontend/src/App.jsx`, `searchTrains`; `frontend/src/api.js` |
| 9 | UI | **Resolved:** the departures heading shows the selected travel date. | `frontend/src/App.jsx`, results heading |
| 10 | Booking | **Resolved:** bookings support multiple named passengers (up to six). | `frontend/src/App.jsx`, passenger list |
| 11 | Booking | Past travel dates are accepted by the form and API. | `frontend/src/App.jsx`, `date`; `backend/src/main/java/com/stlabs/railway/service/BookingService.java`, `create` |
| 12 | Booking | Passenger name is not trimmed or length-limited. | `backend/src/main/java/com/stlabs/railway/dto/BookingRequest.java`; `BookingService.create` |
| 13 | Booking | Rapid repeated form submission can create duplicate bookings; there is no in-flight guard. | `frontend/src/App.jsx`, `book` |
| 14 | Dialog | Escape does not close the booking dialog and focus is not managed as a modal. | `frontend/src/App.jsx`, modal |
| 15 | Cancellation | **Resolved:** cancellation restores the seats for the outbound and return dates. | `backend/src/main/java/com/stlabs/railway/service/BookingService.java`, `cancel` |
| 16 | Cancellation | A cancelled booking can be cancelled repeatedly without conflict. | `backend/src/main/java/com/stlabs/railway/service/BookingService.java`, `cancel` |
| 17 | API | Unknown booking IDs raise `IllegalArgumentException` and are not mapped to a deliberate 404. | `BookingService.cancel`; `BookingController.cancel` |
| 18 | API | Unknown train IDs raise `IllegalArgumentException` and are not mapped to a deliberate 404. | `BookingService.create` |
| 19 | Data | Concurrent first-time bookings for a train/date can race while creating its availability row; the row lock only protects an existing row. | `BookingService.create`; `TrainAvailabilityRepository.findForUpdate` |
| 20 | Data | Duplicate reservations for the same passenger, train, and date are not detected. | `BookingService.create`; `BookingRepository` |
| 21 | Data | `GET /api/bookings` has no explicit order; clients receive repository/database order. | `BookingService.findAll` |
| 22 | Data | `GET /api/trains` loads the complete table without pagination. | `TrainController.all` |
| 23 | API | There is no train search/filter API; all filtering is in the browser. | `TrainController` |
| 24 | API | **Resolved:** all seeded services run daily, and availability is looked up for the requested date. | `TrainController`; `TrainService` |
| 25 | API | Booking cancellation uses `POST` instead of an HTTP `DELETE`/state resource contract. | `BookingController.cancel` |
| 26 | API | Error status and response format are not mapped consistently for validation and domain errors. | `BookingController`; no exception advice |
| 27 | Health | Health always reports `UP`, even if MySQL is unavailable. | `HealthController.health` |
| 28 | Seed data | **Resolved:** startup adds sample trains missing by code, including for previously seeded databases. | `DataSeeder.seedTrains` |
| 29 | Validation | **Resolved:** seat counts are limited to six, matching the passenger-list control. | `BookingRequest` |
| 30 | Validation | **Resolved:** passenger names and email addresses now have maximum lengths. | `BookingRequest` |
| 31 | Schedule | Future dates are valid because the seeded routes run daily; test that each date has independent availability. | `TrainController`; `TrainService` |
| 32 | UI | Cancellation has no pending/disabled state, allowing repeated cancellation requests while the first is in flight. | `frontend/src/App.jsx`, `cancel` |
| 33 | UI | Cancellation is immediate with no confirmation or undo path. | `frontend/src/App.jsx`, `cancel` |
| 34 | UI | Error responses are reduced to a `message` field or generic status; validation detail is lost. | `frontend/src/api.js`, `request` |
| 35 | Search | Search text is not trimmed or normalized before matching. | `frontend/src/App.jsx`, `filtered` |
| 36 | Responsive UI | At narrow widths some route/availability/fare details are hidden rather than reorganized. | `frontend/src/styles.css`, `@media(max-width:560px)` |
| 37 | Accessibility | Modal does not trap/restore focus or announce itself as a dialog; icon and status semantics are incomplete. | `frontend/src/App.jsx`, modal and status |
| 38 | Docs | The app’s exercise link opens a short pointer page rather than the complete 40-exercise handout. | `frontend/public/docs/STUDENT_EXERCISES.md` |
| 39 | UI | The sidebar trip badge counts cancelled and confirmed bookings, while the dashboard metric counts only confirmed bookings. | `frontend/src/App.jsx`, `side-count` and `confirmed` |
| 40 | UI | `Promise.all` treats the two dashboard requests as one unit; if either fails, a successful sibling response is discarded and prior state remains stale. | `frontend/src/App.jsx`, `refresh` |

This answer key preserves the original 40 exercise identifiers. Rows marked **Resolved** describe findings fixed as the portal gained daily schedules, date-specific inventory, round trips, and group bookings. Instructors may have learners assign severity and distinguish symptoms from root causes.
