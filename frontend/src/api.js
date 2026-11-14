const API_BASE = import.meta.env.VITE_API_BASE_URL ?? '';

function csrfToken() {
  const tokenCookie = document.cookie.split('; ').find((cookie) => cookie.startsWith('XSRF-TOKEN='));
  return tokenCookie ? decodeURIComponent(tokenCookie.slice('XSRF-TOKEN='.length)) : '';
}

async function request(path, options = {}) {
  const method = options.method ?? 'GET';
  const headers = { 'Content-Type': 'application/json', ...options.headers };
  if (!['GET', 'HEAD', 'OPTIONS'].includes(method.toUpperCase())) {
    const token = csrfToken();
    if (token) headers['X-XSRF-TOKEN'] = token;
  }
  const response = await fetch(`${API_BASE}/api${path}`, {
    ...options,
    credentials: 'include',
    headers,
  });
  if (!response.ok) {
    const body = await response.json().catch(() => ({}));
    throw new Error(body.message || `Request failed (${response.status})`);
  }
  if (response.status === 204) return null;
  return response.json();
}

export const api = {
  csrf: () => request('/auth/csrf'),
  me: () => request('/auth/me'),
  login: (credentials) => request('/auth/login', { method: 'POST', body: JSON.stringify(credentials) }),
  signup: (account) => request('/auth/signup', { method: 'POST', body: JSON.stringify(account) }),
  logout: () => request('/auth/logout', { method: 'POST' }),
  trains: (date) => request(`/trains${date ? `?date=${encodeURIComponent(date)}` : ''}`),
  bookings: () => request('/bookings'),
  booking: (id) => request(`/bookings/${encodeURIComponent(id)}`),
  createBooking: (booking) =>
    request('/bookings', { method: 'POST', body: JSON.stringify(booking) }),
  cancelBooking: (id, refundMethod) =>
    request(`/bookings/${id}/cancel`, { method: 'POST', body: JSON.stringify({ refundMethod }) }),
  hotels: (city) => request(`/hotels?city=${encodeURIComponent(city)}`),
  hotelBookings: () => request('/hotel-bookings'),
  createHotelBooking: (booking) =>
    request('/hotel-bookings', { method: 'POST', body: JSON.stringify(booking) }),
};
