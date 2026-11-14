# Student testing exercises

## Lab objective

Test a small railway booking workflow end to end. Practice exploratory testing, test design, API testing, UI/accessibility checks, regression, and clear defect reporting. The application is intentionally seeded with **40 defects**. This document describes the behaviors to investigate but does not identify their locations or fixes.

Run the app using the [setup guide](../README.md). Use only sample data. Keep evidence (steps, expected/actual results, severity, and screenshots or API requests) for each finding. A finding should be reproducible and distinct; avoid submitting the same root cause several times.

## Exercises

### Search and schedule
1. Compare the dashboard’s displayed calendar date with the machine’s current date.
2. Check the default journey date near a local-midnight/time-zone boundary.
3. Search for a station using different letter casing.
4. Try choosing the same station for both ends of a journey.
5. Change the origin after choosing a destination that it does not serve.
6. Test the trip-type control and determine whether the selected option affects results.
7. Test the displayed keyboard shortcut for the train search.
8. Select a future journey date and verify that the displayed departures are for that date.
9. Compare the heading for departures with the date selected in the form.
10. Check how station lists behave when two names differ only by letter casing.

### Booking and cancellation
11. Try booking with a past travel date, both through the UI and API.
12. Add several passengers with the Add passenger control; verify the total fare and seat count.
13. Submit a passenger name with leading/trailing whitespace.
14. Check behavior for a very long passenger name.
15. Double-submit the booking action quickly and inspect the resulting bookings.
16. Close the booking dialog using the Escape key.
17. Cancel a booking and compare the availability for that travel date before and after.
18. Cancel the same booking more than once.
19. Try cancelling an unknown booking ID; inspect status and error response.
20. Try booking for an unknown train ID; inspect status and error response.

### API and data integrity
21. Create a booking for a train with no seats remaining on the selected travel date.
22. Submit zero and negative seat counts.
23. Submit a seat count much larger than remaining availability.
24. Submit malformed, missing, and boundary values in booking JSON.
25. Fetch trains and bookings repeatedly; verify ordering and consistency.
26. Inspect whether train listing supports filtering and pagination.
27. Send two simultaneous requests that compete for the last available seat on one date.
28. Create multiple bookings for the same passenger and journey.
29. Check health endpoint behavior while the database is unavailable.
30. Start with a partially populated train table and observe the seed behavior.

### UI, accessibility, and resilience
31. Book the same train on two different dates and compare each date’s available-seat count.
32. Try a booking flow using only the keyboard, including focus when the dialog opens/closes.
33. Test the trip cancellation flow and whether an accidental cancellation can be reversed.
34. Make an API request that fails validation and inspect the error body presented to the user.
35. Search with surrounding spaces and unusual characters.
36. Use a narrow viewport and check all train details and actions remain available.
37. Navigate the page with a screen reader or accessibility tree; check names and status announcements.
38. Open the student-exercise link from the app and confirm it is available in the running frontend.
39. Reload after making and cancelling bookings; compare dashboard counts with the booking list.
40. Repeat the main flow after a failed request and verify stale errors or notices are cleared.

## Supplemental insurance and hotel exercises

- Add the optional insurance during a train booking; check the ₹1-per-passenger premium and displayed policy number.
- Try invalid insurance inputs (non-matching insured name, today's/future birth date, malformed email/mobile, missing address, identical nominee) in both the UI and API.
- Verify multi-passenger bookings receive one policy number and the stated accidental cover per passenger.
- Browse hotels for a city in a train booking; verify other cities are not selectable in the portal.
- Book a sample stay, check the nightly-rate calculation and confirmation number, then reload and verify the reservation remains listed.
- Try a past check-in, checkout on/before check-in, invalid guest count, and unknown hotel ID through the API.
- Sign in as a different user and confirm hotel reservations are isolated to the account that created them.

## Defect report template

- **Title / ID:**
- **Environment:** browser, OS, viewport, app version
- **Preconditions / test data:**
- **Steps to reproduce:**
- **Expected result:**
- **Actual result:**
- **Severity and rationale:**
- **Evidence:** screenshot, response status/body, or relevant console output
- **Reproducibility:** always / intermittent

> Instructor: the matching numbered answer key is in [`INSTRUCTOR_ANSWER_KEY.md`](INSTRUCTOR_ANSWER_KEY.md). Keep it out of student distributions.
