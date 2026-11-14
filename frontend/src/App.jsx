import { useEffect, useMemo, useState } from 'react';
import {
  ArrowDownUp, ArrowRight, Bell, Building2, CalendarDays, Check, CircleHelp,
  Clock3, Coffee, CreditCard, ExternalLink, Globe2, MapPin, Plus, Search, ShieldCheck, Ticket,
  TrainFront, UserRoundPlus, X,
} from 'lucide-react';
import { api } from './api.js';

const money = (amount) => new Intl.NumberFormat('en-IN', {
  style: 'currency', currency: 'INR', maximumFractionDigits: 0,
}).format(amount);
const localDateString = (value) => {
  const year = value.getFullYear();
  const month = String(value.getMonth() + 1).padStart(2, '0');
  const day = String(value.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};
const cityFromStation = (station) => station.replace(/ (Central|Junction|Terminal|City)$/, '');

function AuthPage({ mode, onModeChange, onSubmit }) {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const isSignup = mode === 'signup';

  async function submit(event) {
    event.preventDefault();
    setError('');
    if (isSignup && password !== confirmPassword) {
      setError('Your passwords do not match.');
      return;
    }
    setSubmitting(true);
    try {
      await onSubmit({ name, email, password });
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <main className="auth-page">
      <section className="auth-visual">
        <img className="auth-visual-photo" src="/railway-hero.jpg" alt="" />
        <div className="auth-visual-overlay" />
        <a className="auth-logo" href="/" aria-label="STLabs Railway home"><img src="/stlabs-logo.jpg" alt="STLabs India — Software Training Institute" /></a>
        <div className="auth-visual-copy">
          <span className="auth-kicker"><span /> THE JOURNEY STARTS HERE</span>
          <h1>Good journeys<br />begin with<br /><em>great connections.</em></h1>
          <p>Discover routes, book your seat, and make the most of every mile.</p>
        </div>
        <div className="auth-visual-footer"><span>STLABS RAILWAY PORTAL</span><a className="photo-credit" href="https://commons.wikimedia.org/wiki/File:Howrah_Puri_Vande_Bharat_Express_standing_at_Bhadrak.jpg" target="_blank" rel="noreferrer">Passenger train photo: Harshul12345 / Wikimedia Commons (CC BY-SA 4.0)</a><span>01 — 09</span></div>
      </section>
      <section className="auth-panel">
        <div className="auth-panel-top"><span>Already a member?</span><button type="button" onClick={() => { setError(''); onModeChange(isSignup ? 'login' : 'signup'); }}>{isSignup ? 'Log in' : 'Create account'}</button></div>
        <div className="auth-form-wrap">
          <div className="auth-form-mark"><TrainFront size={20} /></div>
          <div className="auth-form-kicker">{isSignup ? 'YOUR NEXT CHAPTER' : 'WELCOME ABOARD'}</div>
          <h2>{isSignup ? 'Create your account' : 'Sign in to STLabs'}</h2>
          <p className="auth-intro">{isSignup ? 'Join us and get ready for your next journey.' : 'Enter your details to access your railway workspace.'}</p>
          <form className="auth-form" onSubmit={submit}>
            {isSignup && <label>Full name<input autoComplete="name" required maxLength="100" value={name} onChange={(event) => setName(event.target.value)} placeholder="e.g. Aanya Sharma" /></label>}
            <label>Email address<input type="email" autoComplete="email" required value={email} onChange={(event) => setEmail(event.target.value)} placeholder="you@example.com" /></label>
            <label>Password<input type="password" autoComplete={isSignup ? 'new-password' : 'current-password'} required minLength={isSignup ? 8 : 1} maxLength="72" value={password} onChange={(event) => setPassword(event.target.value)} placeholder={isSignup ? 'At least 8 characters' : 'Enter your password'} /></label>
            {isSignup && <label>Confirm password<input type="password" autoComplete="new-password" required minLength="8" maxLength="72" value={confirmPassword} onChange={(event) => setConfirmPassword(event.target.value)} placeholder="Re-enter your password" /></label>}
            {!isSignup && <div className="auth-form-options"><span><ShieldCheck size={14} /> Passwords are securely encrypted</span><span>Student workspace</span></div>}
            {error && <div className="auth-error" role="alert">{error}</div>}
            <button type="submit" className="auth-submit" disabled={submitting}>{submitting ? 'Please wait…' : isSignup ? 'Create account' : 'Sign in'} <ArrowRight size={17} /></button>
          </form>
          <div className="auth-divider"><span /> <small>STLABS RAILWAY</small> <span /></div>
          <p className="auth-terms">Your account is used to access the STLabs Railway testing workspace.</p>
        </div>
        <div className="auth-panel-footer"><span>© 2026 STLabs India</span><a href="mailto:help@stlabs.example">Need help?</a></div>
      </section>
    </main>
  );
}

function App() {
  const [account, setAccount] = useState(null);
  const [authMode, setAuthMode] = useState('login');
  const [authLoading, setAuthLoading] = useState(true);
  const [trains, setTrains] = useState([]);
  const [returnDayTrains, setReturnDayTrains] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [query, setQuery] = useState('');
  const [from, setFrom] = useState('');
  const [to, setTo] = useState('');
  const [date, setDate] = useState(() => localDateString(new Date()));
  const [tripType, setTripType] = useState('oneWay');
  const [returnDate, setReturnDate] = useState(() => {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    return localDateString(tomorrow);
  });
  const [searchedRoute, setSearchedRoute] = useState({ from: '', to: '' });
  const [searchedTripType, setSearchedTripType] = useState('oneWay');
  const [passengers, setPassengers] = useState(['']);
  const [email, setEmail] = useState('');
  const [selectedTrain, setSelectedTrain] = useState(null);
  const [selectedOutboundTrain, setSelectedOutboundTrain] = useState(null);
  const [selectedReturnTrain, setSelectedReturnTrain] = useState(null);
  const [notice, setNotice] = useState('');
  const [activeTab, setActiveTab] = useState('trains');
  const [bookingIdQuery, setBookingIdQuery] = useState('');
  const [cancellationBooking, setCancellationBooking] = useState(null);
  const [refundMethod, setRefundMethod] = useState('ORIGINAL_ACCOUNT');
  const [cancellationError, setCancellationError] = useState('');
  const [lookingUpBooking, setLookingUpBooking] = useState(false);
  const [cancellingBooking, setCancellingBooking] = useState(false);
  const [includeInsurance, setIncludeInsurance] = useState(false);
  const [mealPreference, setMealPreference] = useState('');
  const [insuranceDetails, setInsuranceDetails] = useState({
    insuredName: '', insuredDateOfBirth: '', insuredGender: '', insuredPhone: '',
    insuredEmail: '', insuredAddress: '', nomineeName: '', nomineeRelationship: '',
    nomineeDateOfBirth: '', nomineePhone: '',
  });
  const [hotelBookings, setHotelBookings] = useState([]);
  const [hotelCity, setHotelCity] = useState('');
  const [hotels, setHotels] = useState([]);
  const [hotelsLoading, setHotelsLoading] = useState(false);
  const [hotelError, setHotelError] = useState('');
  const [selectedHotel, setSelectedHotel] = useState(null);
  const [hotelCheckIn, setHotelCheckIn] = useState(() => localDateString(new Date()));
  const [hotelCheckOut, setHotelCheckOut] = useState(() => {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    return localDateString(tomorrow);
  });
  const [hotelGuests, setHotelGuests] = useState(1);
  const [hotelGuestName, setHotelGuestName] = useState('');
  const [hotelEmail, setHotelEmail] = useState('');
  const [hotelSubmitting, setHotelSubmitting] = useState(false);

  async function refresh() {
    setError('');
    try {
      const [trainRows, bookingRows, returnRows, hotelBookingRows] = await Promise.all([
        api.trains(date),
        api.bookings(),
        api.trains(returnDate),
        api.hotelBookings(),
      ]);
      setTrains(trainRows);
      setBookings(bookingRows);
      setReturnDayTrains(returnRows);
      setHotelBookings(hotelBookingRows);
    } catch (e) {
      setError(`${e.message}. Start the backend and MySQL using the setup guide.`);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    api.csrf()
      .then(() => api.me())
      .then((currentAccount) => {
        setAccount(currentAccount);
        setAuthLoading(false);
        return refresh();
      })
      .catch(() => setAuthLoading(false));
  }, []);

  async function authenticate(credentials) {
    const signedIn = authMode === 'signup'
      ? await api.signup(credentials)
      : await api.login(credentials);
    setAccount(signedIn);
    setAuthLoading(false);
    await refresh();
  }

  async function signOut() {
    try {
      await api.logout();
      setAccount(null);
      setBookings([]);
      setAuthMode('login');
    } catch (e) {
      setNotice(e.message);
    }
  }

  const filtered = useMemo(() => trains.filter((train) => {
    const text = `${train.code} ${train.name} ${train.origin} ${train.destination}`.toLowerCase();
    return text.includes(query.toLowerCase())
      && (!searchedRoute.from || train.origin === searchedRoute.from)
      && (!searchedRoute.to || train.destination === searchedRoute.to);
  }), [trains, query, searchedRoute]);
  const returnTrains = useMemo(() => selectedOutboundTrain
    ? returnDayTrains.filter((train) => train.origin === selectedOutboundTrain.destination
      && train.destination === selectedOutboundTrain.origin
      && `${train.code} ${train.name} ${train.origin} ${train.destination}`.toLowerCase().includes(query.toLowerCase()))
    : [], [returnDayTrains, selectedOutboundTrain, query]);

  const stations = [...new Set(trains.flatMap((train) => [train.origin, train.destination]))];
  const confirmed = bookings.filter((booking) => booking.status === 'CONFIRMED').length;
  const activeBookings = bookings.filter((booking) => booking.status === 'CONFIRMED');
  const hotelCities = [...new Set(bookings
    .filter((booking) => booking.email.toLowerCase() === account.email.toLowerCase())
    .map((booking) => cityFromStation(booking.destination)))];

  useEffect(() => {
    if (activeTab !== 'hotels' || !hotelCity) return;
    let active = true;
    setHotelsLoading(true);
    setHotelError('');
    api.hotels(hotelCity)
      .then((rows) => { if (active) setHotels(rows); })
      .catch((requestError) => { if (active) setHotelError(requestError.message); })
      .finally(() => { if (active) setHotelsLoading(false); });
    return () => { active = false; };
  }, [activeTab, hotelCity]);

  async function book(event) {
    event.preventDefault();
    if (!selectedTrain) return;
    if (passengers.some((name) => name.trim().length < 2)) {
      setNotice('Enter a valid name for every passenger.');
      return;
    }
    if (includeInsurance && insuranceDetails.insuredName.trim().toLowerCase() !== passengers[0].trim().toLowerCase()) {
      setNotice('The insured person name must match Passenger 1.');
      return;
    }
    if (includeInsurance && insuranceDetails.nomineeName.trim().toLowerCase() === insuranceDetails.insuredName.trim().toLowerCase()) {
      setNotice('The nominee must be a different person from the insured passenger.');
      return;
    }
    try {
      const booking = await api.createBooking({
        trainId: selectedTrain.id,
        passengerName: passengers[0],
        passengerNames: passengers.map((name) => name.trim()),
        email,
        seats: passengers.length,
        travelDate: date,
        ...(mealPreference && { mealPreference }),
        ...(selectedReturnTrain && { returnTrainId: selectedReturnTrain.id, returnDate }),
        ...(includeInsurance && { travelInsurance: insuranceDetails }),
      });
      const mealLabel = { VEG: 'Vegetarian', NON_VEG: 'Non-vegetarian', JAIN: 'Jain' }[booking.mealPreference];
      setNotice(booking.insurancePolicyNumber
        ? `Booking confirmed${mealLabel ? ` · ${mealLabel} meal selected for all passengers` : ''}. Insurance policy ${booking.insurancePolicyNumber} issued with ₹2,00,000 accidental cover per passenger. Premium: ${money(booking.insurancePremium)}.`
        : `${passengers.length} passenger${passengers.length === 1 ? '' : 's'} booked${mealLabel ? ` · ${mealLabel} meal selected for all passengers` : ''}. Your trip is now in My trips.`);
      setSelectedTrain(null);
      setSelectedOutboundTrain(null);
      setSelectedReturnTrain(null);
      setPassengers(['']);
      setEmail('');
      setMealPreference('');
      setIncludeInsurance(false);
      setInsuranceDetails({
        insuredName: '', insuredDateOfBirth: '', insuredGender: '', insuredPhone: '',
        insuredEmail: '', insuredAddress: '', nomineeName: '', nomineeRelationship: '',
        nomineeDateOfBirth: '', nomineePhone: '',
      });
      await refresh();
    } catch (e) {
      setNotice(e.message);
    }

  }

  async function bookHotel(event) {
    event.preventDefault();
    if (!selectedHotel) return;
    setHotelError('');
    setHotelSubmitting(true);
    try {
      const reservation = await api.createHotelBooking({
        hotelId: selectedHotel.id,
        checkIn: hotelCheckIn,
        checkOut: hotelCheckOut,
        guests: Number(hotelGuests),
        guestName: hotelGuestName,
        email: hotelEmail,
      });
      setNotice(`Hotel reservation confirmed: ${reservation.confirmationNumber}.`);
      setSelectedHotel(null);
      await refresh();
    } catch (requestError) {
      setHotelError(requestError.message);
    } finally {
      setHotelSubmitting(false);
    }
  }

  async function lookupBooking(event) {
    event.preventDefault();
    setCancellationError('');
    setCancellationBooking(null);
    setLookingUpBooking(true);
    try {
      const booking = await api.booking(bookingIdQuery.trim());
      setCancellationBooking(booking);
      setRefundMethod('ORIGINAL_ACCOUNT');
    } catch (e) {
      setCancellationError(e.message);
    } finally {
      setLookingUpBooking(false);
    }
  }

  function refundMethodLabel(method) {
    return {
      DEBIT_CARD: 'debit card',
      CREDIT_CARD: 'credit card',
      ORIGINAL_ACCOUNT: 'the original booking account',
    }[method] || 'the selected method';
  }

  async function cancelBooking() {
    if (!cancellationBooking || cancellationBooking.status !== 'CONFIRMED') return;
    setCancellationError('');
    setCancellingBooking(true);
    try {
      const cancelledBooking = await api.cancelBooking(cancellationBooking.id, refundMethod);
      setCancellationBooking(cancelledBooking);
      setNotice(`Booking #${cancelledBooking.id} cancelled. Your dummy refund method is ${refundMethodLabel(cancelledBooking.refundMethod)}.`);
      await refresh();
    } catch (e) {
      setCancellationError(e.message);
    } finally {
      setCancellingBooking(false);
    }
  }

  function swapRoute() {
    setFrom(to);
    setTo(from);
  }

  function searchTrains(event) {
    event.preventDefault();
    if (tripType === 'roundTrip' && returnDate < date) {
      setNotice('Return date must be the same as or after the journey date.');
      return;
    }
    setSearchedRoute({ from, to });
    setSearchedTripType(tripType);
    setSelectedOutboundTrain(null);
    setSelectedReturnTrain(null);
    setNotice('');
    setActiveTab('trains');
    Promise.all([api.trains(date), api.trains(tripType === 'roundTrip' ? returnDate : date)])
      .then(([outboundRows, returnRows]) => {
        setTrains(outboundRows);
        setReturnDayTrains(returnRows);
      })
      .catch((requestError) => setError(requestError.message));
  }

  function selectOutbound(train) {
    setSelectedOutboundTrain(train);
    setSelectedReturnTrain(null);
  }

  function selectReturn(train) {
    setSelectedReturnTrain(train);
  }

  function setPassengerName(index, name) {
    setPassengers((current) => current.map((passengerName, passengerIndex) =>
      passengerIndex === index ? name : passengerName));
  }

  function setInsuranceField(field, value) {
    setInsuranceDetails((current) => ({ ...current, [field]: value }));
  }

  if (authLoading) {
    return <div className="auth-loading"><img src="/stlabs-logo.jpg" alt="STLabs India" /><span>Getting your journey ready…</span></div>;
  }

  if (!account) {
    return <AuthPage mode={authMode} onModeChange={setAuthMode} onSubmit={authenticate} />;
  }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <a className="brand" href="#" aria-label="STLabs Railway home">
          <img src="/stlabs-logo.jpg" alt="STLabs India" />
          <span className="brand-sub">RAILWAY PORTAL</span>
        </a>
        <div className="side-label">TRAVEL</div>
        <button className={`side-link ${activeTab === 'trains' ? 'selected' : ''}`} onClick={() => setActiveTab('trains')}>
          <Search size={18} /> Find a train
        </button>
        <button className={`side-link ${activeTab === 'trips' ? 'selected' : ''}`} onClick={() => setActiveTab('trips')}>
          <Ticket size={18} /> My trips <span className="side-count">{activeBookings.length}</span>
        </button>
        <button className={`side-link ${activeTab === 'hotels' ? 'selected' : ''}`} onClick={() => { setActiveTab('hotels'); if (!hotelCity && hotelCities.length) setHotelCity(hotelCities[0]); }}>
          <Building2 size={18} /> Hotels
        </button>
        <a className="side-link" href="https://stlabsindia.com/privacy-policy" target="_blank" rel="noreferrer">
          <CircleHelp size={18} /> Help & support <ExternalLink size={13} />
        </a>
        <div className="side-bottom">
          <div className="lab-card">
            <div className="lab-icon"><ShieldCheck size={17} /></div>
            <strong>Testing lab</strong>
            <p>Practice, explore, and report bugs.</p>
            <a href="/docs/STUDENT_EXERCISES.md" target="_blank" rel="noreferrer">View exercises <ArrowRight size={13} /></a>
          </div>
          <div className="profile">
            <div className="avatar">{account.name.slice(0, 1).toUpperCase()}</div><div><strong>{account.name}</strong><small>{account.email}</small></div>
            <button type="button" className="profile-logout" onClick={signOut}>Sign out</button>
          </div>
        </div>
      </aside>

      <main className="main-content">
        <header className="topbar">
          <div className="breadcrumbs"><span>Workspace</span><span className="crumb-slash">/</span><strong>Railway portal</strong></div>
          <div className="top-actions">
            <a className="header-website" href="https://www.stlabsindia.com" target="_blank" rel="noreferrer"><Globe2 size={15} aria-hidden="true" />www.stlabsindia.com<ExternalLink size={12} aria-hidden="true" /></a>
            <a className="header-website assessment-link" href="https://software-testing-online-test.vercel.app" target="_blank" rel="noreferrer"><ExternalLink size={15} aria-hidden="true" />Take a Software Testing assessment<ExternalLink size={12} aria-hidden="true" /></a>
            <span className="environment"><span /> LAB ENVIRONMENT</span>
            <button className="icon-button" aria-label="Notifications" onClick={() => setNotice('You are all caught up.')}><Bell size={18} /><i /></button>
            <div className="top-avatar">{account.name.slice(0, 1).toUpperCase()}</div>
          </div>
        </header>

        <div className="page">
          <section className="welcome-row">
            <img className="welcome-photo" src="/railway-hero.jpg" alt="Passenger train travelling through the countryside" />
            <div className="welcome-shade" />
            <div className="welcome-text"><div className="eyebrow">YOUR NEXT ADVENTURE</div><h1>Every journey<br />has a story.</h1><p className="welcome-copy">Find your train, reserve your seat, and enjoy the ride.</p></div>
            <a className="photo-credit welcome-photo-credit" href="https://commons.wikimedia.org/wiki/File:Howrah_Puri_Vande_Bharat_Express_standing_at_Bhadrak.jpg" target="_blank" rel="noreferrer">Photo: Harshul12345 / Wikimedia Commons (CC BY-SA 4.0)</a>
          </section>

          <form className="search-panel" onSubmit={searchTrains}>
            <div className="panel-title"><span className="panel-title-icon"><Search size={16} /></span><strong>Plan your journey</strong>
              <div className="trip-type-toggle" role="group" aria-label="Trip type">
                <button type="button" className={tripType === 'oneWay' ? 'active' : ''} onClick={() => { setTripType('oneWay'); setSelectedOutboundTrain(null); setSelectedReturnTrain(null); }}>One way</button>
                <button type="button" className={tripType === 'roundTrip' ? 'active' : ''} onClick={() => { setTripType('roundTrip'); setSelectedOutboundTrain(null); setSelectedReturnTrain(null); }}>Round trip</button>
              </div>
            </div>
            <div className="search-fields">
              <label className="field"><span>FROM</span><div><MapPin size={17} /><select aria-label="From station" value={from} onChange={(e) => setFrom(e.target.value)}><option value="">Any station</option>{stations.map((station) => <option key={station}>{station}</option>)}</select></div></label>
              <button type="button" className="swap-button" aria-label="Swap stations" onClick={swapRoute}><ArrowDownUp size={16} /></button>
              <label className="field"><span>TO</span><div><MapPin size={17} /><select aria-label="To station" value={to} onChange={(e) => setTo(e.target.value)}><option value="">Any station</option>{stations.map((station) => <option key={station}>{station}</option>)}</select></div></label>
              <div className={`date-fields ${tripType === 'roundTrip' ? 'with-return' : ''}`}>
                <label className="field date-field"><span>JOURNEY DATE</span><div><CalendarDays size={17} /><input type="date" aria-label="Journey date" value={date} onChange={(e) => { setDate(e.target.value); if (returnDate < e.target.value) setReturnDate(e.target.value); }} /></div></label>
                {tripType === 'roundTrip' && <label className="field date-field return-date-field"><span>RETURN DATE</span><div><CalendarDays size={17} /><input type="date" aria-label="Return date" min={date} value={returnDate} onChange={(e) => setReturnDate(e.target.value)} /></div></label>}
              </div>
              <button type="submit" className="search-button"><Search size={17} /> Search trains</button>
            </div>
          </form>

          <div className="stats-row">
            <div className="stat-card"><span className="stat-icon blue"><TrainFront size={18} /></span><div><span>Scheduled trains</span><strong>{trains.length}</strong></div><span className="stat-note">Across the network</span></div>
            <div className="stat-card"><span className="stat-icon green"><Ticket size={18} /></span><div><span>Active bookings</span><strong>{confirmed}</strong></div><span className="stat-note">Ready to travel</span></div>
            <div className="stat-card"><span className="stat-icon orange"><MapPin size={18} /></span><div><span>Connected stations</span><strong>{stations.length}</strong></div><span className="stat-note">And growing</span></div>
          </div>

          <section className="results-section">
            <div className="section-heading">
              <div><h2>{activeTab === 'trips' ? 'My trips' : activeTab === 'hotels' ? 'Destination hotels' : searchedTripType === 'roundTrip' ? 'Choose your journey' : 'Daily departures'}</h2><p>{activeTab === 'trips' ? 'Your active reservations and ticket cancellation tools.' : activeTab === 'hotels' ? 'Browse and book sample stays in your train booking destination cities.' : `Direct trains across all station pairs · ${date}${searchedTripType === 'roundTrip' ? ` · Return ${returnDate}` : ''}`}</p></div>
              {activeTab === 'trains' && <label className="quick-search"><Search size={16} /><input aria-label="Search trains" placeholder="Search trains or stations" value={query} onChange={(e) => setQuery(e.target.value)} /><kbd>⌘ K</kbd></label>}
            </div>
            {error && <div className="error-banner" role="alert">{error}</div>}
            {notice && <div className="notice-banner" role="status"><Check size={16} />{notice}<button onClick={() => setNotice('')} aria-label="Dismiss"><X size={15} /></button></div>}
            {loading ? <div className="empty-state">Loading the railway network…</div> : activeTab === 'trips' ? (
              <div className="booking-list">
                <section className="cancel-lookup-panel">
                  <div className="cancel-lookup-heading">
                    <span className="cancel-lookup-icon"><Search size={17} /></span>
                    <div><strong>Cancel a ticket</strong><p>Enter your unique booking ID to view its details and request a dummy refund.</p></div>
                  </div>
                  <form className="cancel-lookup-form" onSubmit={lookupBooking}>
                    <label className="form-label">Booking ID<input required type="number" min="1" step="1" value={bookingIdQuery} onChange={(event) => setBookingIdQuery(event.target.value)} placeholder="e.g. 1042" /></label>
                    <button className="lookup-booking-button" type="submit" disabled={lookingUpBooking}><Search size={15} />{lookingUpBooking ? 'Searching…' : 'Find booking'}</button>
                  </form>
                  {cancellationError && <div className="cancellation-error" role="alert">{cancellationError}</div>}
                  {cancellationBooking && <div className="cancellation-details">
                    <div className="cancellation-detail-heading"><div><span>BOOKING #{cancellationBooking.id}</span><strong>{cancellationBooking.trainName}</strong></div><div className={`status-pill ${cancellationBooking.status.toLowerCase()}`}>{cancellationBooking.status}</div></div>
                    <div className="cancellation-route"><span>{cancellationBooking.origin} <ArrowRight size={13} /> {cancellationBooking.destination}</span><small>Journey date · {cancellationBooking.travelDate}</small></div>
                    {cancellationBooking.returnTrainName && <div className="cancellation-route"><span>{cancellationBooking.returnOrigin} <ArrowRight size={13} /> {cancellationBooking.returnDestination}</span><small>Return date · {cancellationBooking.returnDate}</small></div>}
                    {cancellationBooking.mealPreference && <div className="cancellation-complete">Meal preference: {{ VEG: 'Vegetarian', NON_VEG: 'Non-vegetarian', JAIN: 'Jain' }[cancellationBooking.mealPreference]} · all passengers</div>}
                    <div className="cancellation-meta"><span>{cancellationBooking.seats} passenger{cancellationBooking.seats === 1 ? '' : 's'} · {(cancellationBooking.passengers || [cancellationBooking.passengerName]).join(', ')}</span><strong>{money(cancellationBooking.totalFare)}</strong></div>
                    {cancellationBooking.status === 'CONFIRMED' ? <div className="refund-choice">
                      <label className="form-label">Dummy refund method<select value={refundMethod} onChange={(event) => setRefundMethod(event.target.value)}>
                        <option value="ORIGINAL_ACCOUNT">Same account used for booking</option>
                        <option value="DEBIT_CARD">Dummy debit card</option>
                        <option value="CREDIT_CARD">Dummy credit card</option>
                      </select></label>
                      <p>Training simulation only. No card or bank details are collected and no real payment is made.</p>
                      <button className="cancel-ticket-button" type="button" onClick={cancelBooking} disabled={cancellingBooking}><X size={15} />{cancellingBooking ? 'Cancelling…' : 'Cancel ticket'}</button>
                    </div> : <p className="cancellation-complete">This ticket has already been cancelled. Refund method: {refundMethodLabel(cancellationBooking.refundMethod)}.</p>}
                  </div>}
                </section>
                {activeBookings.length === 0 ? <div className="empty-state">No active trips. Find a train to make your first booking.</div> : activeBookings.map((booking) => (
                  <article className="booking-card" key={booking.id}>
                    <div className="booking-ticket"><Ticket size={20} /></div>
                    <div className="booking-info"><strong>{booking.trainName}</strong><span>{booking.origin} <ArrowRight size={13} /> {booking.destination} · {booking.travelDate}</span>{booking.returnTrainName && <span className="booking-return">{booking.returnTrainName}: {booking.returnOrigin} <ArrowRight size={13} /> {booking.returnDestination} · {booking.returnDate}</span>}<small>Booking ID #{booking.id} · {booking.seats} passenger{booking.seats === 1 ? '' : 's'} · {(booking.passengers || [booking.passengerName]).join(', ')}</small>{booking.mealPreference && <small className="meal-chip">Meal preference: {{ VEG: 'Vegetarian', NON_VEG: 'Non-vegetarian', JAIN: 'Jain' }[booking.mealPreference]} · all passengers</small>}{booking.insurancePolicyNumber && <small className="policy-chip">Insurance policy {booking.insurancePolicyNumber} · ₹2,00,000 cover per passenger</small>}</div>
                    <div className={`status-pill ${booking.status.toLowerCase()}`}>{booking.status}</div>
                    <strong className="booking-price">{money(booking.totalFare)}</strong>
                  </article>
                ))}
              </div>
            ) : activeTab === 'hotels' ? (
              <div className="hotel-content">
                {hotelCities.length === 0 ? <div className="empty-state">Book a train first to browse hotels in its destination city.</div> : <>
                  <div className="hotel-toolbar"><label className="form-label">Train destination city<select aria-label="Hotel destination city" value={hotelCity} onChange={(event) => setHotelCity(event.target.value)}>{hotelCities.map((city) => <option key={city} value={city}>{city}</option>)}</select></label><span>Sample listings · prices are simulated</span></div>
                  {hotelError && <div className="error-banner" role="alert">{hotelError}</div>}
                  {hotelsLoading ? <div className="empty-state">Finding stays in {hotelCity}…</div> : <div className="hotel-grid">
                    {hotels.map((hotel) => <article className="hotel-card" key={hotel.id}>
                      <div className="hotel-photo"><Building2 size={29} /><span>{hotel.rating} ★</span></div>
                      <div className="hotel-card-body"><span className="hotel-city-label">{hotel.city} · SAMPLE HOTEL</span><h3>{hotel.name}</h3><p>{hotel.description}</p><small><MapPin size={13} /> {hotel.address}</small>
                        <div className="hotel-card-footer"><strong>{money(hotel.nightlyRate)}<small> / night</small></strong><button className="book-button" type="button" onClick={() => { setSelectedHotel(hotel); setHotelGuestName(account.name); setHotelEmail(account.email); setHotelError(''); }}>Book stay <ArrowRight size={15} /></button></div>
                      </div>
                    </article>)}
                  </div>}
                </>}
                <div className="hotel-reservations">
                  <div className="section-heading"><div><h2>Your hotel bookings</h2><p>Saved sample stays and confirmation numbers.</p></div></div>
                  {hotelBookings.length === 0 ? <div className="empty-state">No hotel stays booked yet.</div> : hotelBookings.map((booking) => <article className="hotel-reservation-card" key={booking.id}>
                    <span className="hotel-reservation-icon"><Building2 size={19} /></span><div className="hotel-reservation-info"><strong>{booking.hotelName}</strong><span>{booking.city} · {booking.checkIn} to {booking.checkOut} · {booking.guests} guest{booking.guests === 1 ? '' : 's'}</span><small>Confirmation {booking.confirmationNumber}</small></div><div className={`status-pill ${booking.status.toLowerCase()}`}>{booking.status}</div><strong>{money(booking.totalPrice)}</strong>
                  </article>)}
                </div>
              </div>
            ) : (
              <div className="train-list">
                {filtered.length === 0 ? <div className="empty-state">No trains match those filters. Try another station or search.</div> : filtered.map((train) => (
                  <article className="train-card" key={train.id}>
                    <div className="train-identity"><div className="train-mark"><TrainFront size={19} /></div><div><strong>{train.name}</strong><span>{train.code} <i /> Express · Daily</span></div></div>
                    <div className="route-block"><strong>{train.departureTime}</strong><span>{train.origin}</span></div>
                    <div className="route-duration"><span /> <small>{train.duration}</small><span /></div>
                    <div className="route-block"><strong>{train.arrivalTime}</strong><span>{train.destination}</span></div>
                    <div className="availability"><span className={train.availableSeats < 10 ? 'few-seats' : ''}>{train.availableSeats} seats</span><small>available daily</small></div>
                    <div className="fare-block"><strong>{money(train.fare)}</strong><span>per passenger</span></div>
                    {searchedTripType === 'roundTrip'
                      ? <button className={`book-button ${selectedOutboundTrain?.id === train.id ? 'chosen' : ''}`} disabled={train.availableSeats < 1} onClick={() => selectOutbound(train)}>{selectedOutboundTrain?.id === train.id ? 'Selected' : 'Outbound'} <ArrowRight size={15} /></button>
                      : <button className="book-button" disabled={train.availableSeats < 1} onClick={() => { setSelectedReturnTrain(null); setSelectedTrain(train); setPassengers(['']); setEmail(account.email); }}>{train.availableSeats < 1 ? 'Full' : 'Book'} <ArrowRight size={15} /></button>}
                  </article>
                ))}
              </div>
            )}
            {activeTab === 'trains' && searchedTripType === 'roundTrip' && selectedOutboundTrain && (
              <div className="return-section">
                <div className="return-heading"><div><span className="step-label">RETURN JOURNEY · {returnDate}</span><h3>{selectedOutboundTrain.destination} <ArrowRight size={16} /> {selectedOutboundTrain.origin}</h3></div><span className="return-count">{returnTrains.length} daily options</span></div>
                {returnTrains.length === 0 ? <div className="empty-state">No return trains are scheduled for this route yet. Try another outbound train.</div> : returnTrains.map((train) => (
                  <article className="train-card return-train-card" key={train.id}>
                    <div className="train-identity"><div className="train-mark return-mark"><TrainFront size={19} /></div><div><strong>{train.name}</strong><span>{train.code} <i /> Return service · Daily</span></div></div>
                    <div className="route-block"><strong>{train.departureTime}</strong><span>{train.origin}</span></div>
                    <div className="route-duration"><span /> <small>{train.duration}</small><span /></div>
                    <div className="route-block"><strong>{train.arrivalTime}</strong><span>{train.destination}</span></div>
                    <div className="availability"><span>{train.availableSeats} seats</span><small>available</small></div>
                    <div className="fare-block"><strong>{money(train.fare)}</strong><span>per passenger</span></div>
                    <button className={`book-button ${selectedReturnTrain?.id === train.id ? 'chosen' : ''}`} disabled={train.availableSeats < 1} onClick={() => selectReturn(train)}>{selectedReturnTrain?.id === train.id ? 'Selected' : 'Return'} <ArrowRight size={15} /></button>
                  </article>
                ))}
                {selectedReturnTrain && <div className="round-trip-summary"><Check size={16} /> Round trip selected <strong>{money(selectedOutboundTrain.fare + selectedReturnTrain.fare)} per passenger</strong><button type="button" onClick={() => { setSelectedTrain(selectedOutboundTrain); setPassengers(['']); setEmail(account.email); }}>Continue to passengers <ArrowRight size={15} /></button></div>}
              </div>
            )}
          </section>
          <footer className="footer"><span>© 2026 STLabs Railway Portal</span><span><Clock3 size={13} /> Times shown in local time</span><span><Coffee size={13} /> Made for curious testers</span></footer>
        </div>
      </main>

      {selectedTrain && <div className="modal-backdrop" onMouseDown={(e) => { if (e.target === e.currentTarget) setSelectedTrain(null); }}>
        <form className="booking-modal" onSubmit={book}>
          <button type="button" className="modal-close" aria-label="Close" onClick={() => setSelectedTrain(null)}><X size={19} /></button>
          <div className="modal-icon"><Ticket size={21} /></div><div className="eyebrow">RESERVE YOUR SEAT</div>
          <h2>{selectedTrain.name}{selectedReturnTrain && ' + return'}</h2>
          <p className="modal-route">{selectedTrain.origin} <ArrowRight size={14} /> {selectedTrain.destination} · {date}</p>
          {selectedReturnTrain && <p className="modal-route return-modal-route">{selectedReturnTrain.origin} <ArrowRight size={14} /> {selectedReturnTrain.destination} · {returnDate}</p>}
          <div className="modal-fare"><span>{passengers.length} passenger{passengers.length === 1 ? '' : 's'} · {selectedReturnTrain ? 'round-trip total' : 'total fare'}</span><strong>{money((selectedTrain.fare + (selectedReturnTrain?.fare || 0)) * passengers.length + (includeInsurance ? passengers.length : 0))}</strong></div>
          <div className="passenger-list">
            {passengers.map((name, index) => <label className="form-label passenger-field" key={index}>Passenger {index + 1} name<input required minLength="2" maxLength="100" value={name} onChange={(e) => setPassengerName(index, e.target.value)} placeholder="e.g. Aanya Sharma" />{passengers.length > 1 && <button type="button" className="remove-passenger" aria-label={`Remove passenger ${index + 1}`} onClick={() => setPassengers((current) => current.filter((_, passengerIndex) => passengerIndex !== index))}><X size={15} /></button>}</label>)}
            {passengers.length < 6 && <button type="button" className="add-passenger" onClick={() => setPassengers((current) => [...current, ''])}><UserRoundPlus size={15} /> Add passenger <Plus size={14} /></button>}
          </div>
          <label className="form-label">Email address<input required type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="you@example.com" /></label>
          <label className="form-label meal-choice">Meal preference (optional)<select value={mealPreference} onChange={(event) => setMealPreference(event.target.value)}><option value="">No meal selected</option><option value="VEG">Vegetarian (Veg)</option><option value="NON_VEG">Non-vegetarian (Non-veg)</option><option value="JAIN">Jain meal</option></select><small>Applies to all passengers on this booking. No extra charge in this training app.</small></label>
          <section className="insurance-option">
            <label className="insurance-toggle"><input type="checkbox" checked={includeInsurance} onChange={(event) => setIncludeInsurance(event.target.checked)} /><span><strong>Add travel insurance</strong><small>₹1 per passenger · up to ₹2,00,000 accidental cover per person</small></span></label>
            {includeInsurance && <div className="insurance-form">
              <p>One policy covers the passengers on this booking. Fill in the lead passenger and nominee details. This is a training simulation, not an insurance contract.</p>
              <div className="insurance-fields">
                <label className="form-label">Insured person name<input required minLength="2" maxLength="100" value={insuranceDetails.insuredName} onChange={(event) => setInsuranceField('insuredName', event.target.value)} placeholder={passengers[0] || 'Must match Passenger 1'} /></label>
                <label className="form-label">Date of birth<input required type="date" max={localDateString(new Date())} value={insuranceDetails.insuredDateOfBirth} onChange={(event) => setInsuranceField('insuredDateOfBirth', event.target.value)} /></label>
                <label className="form-label">Gender<select required value={insuranceDetails.insuredGender} onChange={(event) => setInsuranceField('insuredGender', event.target.value)}><option value="">Select</option><option value="MALE">Male</option><option value="FEMALE">Female</option><option value="OTHER">Other</option></select></label>
                <label className="form-label">Mobile number<input required type="tel" inputMode="numeric" pattern="[6-9][0-9]{9}" title="Enter a valid 10-digit Indian mobile number" maxLength="10" value={insuranceDetails.insuredPhone} onChange={(event) => setInsuranceField('insuredPhone', event.target.value)} placeholder="10-digit Indian mobile" /></label>
                <label className="form-label">Email address<input required type="email" maxLength="254" value={insuranceDetails.insuredEmail} onChange={(event) => setInsuranceField('insuredEmail', event.target.value)} placeholder="name@example.com" /></label>
                <label className="form-label insurance-address">Residential address<textarea required minLength="8" maxLength="250" value={insuranceDetails.insuredAddress} onChange={(event) => setInsuranceField('insuredAddress', event.target.value)} placeholder="Enter your complete address" /></label>
              </div>
              <h3>Nominee details</h3>
              <div className="insurance-fields">
                <label className="form-label">Nominee full name<input required minLength="2" maxLength="100" value={insuranceDetails.nomineeName} onChange={(event) => setInsuranceField('nomineeName', event.target.value)} placeholder="Nominee name" /></label>
                <label className="form-label">Relationship<select required value={insuranceDetails.nomineeRelationship} onChange={(event) => setInsuranceField('nomineeRelationship', event.target.value)}><option value="">Select relationship</option><option value="SPOUSE">Spouse</option><option value="PARENT">Parent</option><option value="CHILD">Child</option><option value="SIBLING">Sibling</option><option value="OTHER">Other</option></select></label>
                <label className="form-label">Nominee date of birth<input required type="date" max={localDateString(new Date())} value={insuranceDetails.nomineeDateOfBirth} onChange={(event) => setInsuranceField('nomineeDateOfBirth', event.target.value)} /></label>
                <label className="form-label">Nominee mobile number<input required type="tel" inputMode="numeric" pattern="[6-9][0-9]{9}" title="Enter a valid 10-digit Indian mobile number" maxLength="10" value={insuranceDetails.nomineePhone} onChange={(event) => setInsuranceField('nomineePhone', event.target.value)} placeholder="10-digit Indian mobile" /></label>
              </div>
            </div>}
          </section>
          <button className="confirm-button" type="submit"><CreditCard size={16} /> Confirm booking</button>
          <small className="secure-note"><ShieldCheck size={13} /> Your booking is stored securely in the lab database</small>
        </form>
      </div>}
      {selectedHotel && <div className="modal-backdrop" onMouseDown={(event) => { if (event.target === event.currentTarget) setSelectedHotel(null); }}>
        <form className="booking-modal hotel-booking-modal" onSubmit={bookHotel}>
          <button type="button" className="modal-close" aria-label="Close hotel booking" onClick={() => setSelectedHotel(null)}><X size={19} /></button>
          <div className="modal-icon"><Building2 size={21} /></div><div className="eyebrow">SAMPLE HOTEL RESERVATION</div>
          <h2>{selectedHotel.name}</h2><p className="modal-route">{selectedHotel.city} · {selectedHotel.address}</p>
          <div className="hotel-form-fields">
            <label className="form-label">Check-in<input required type="date" min={localDateString(new Date())} value={hotelCheckIn} onChange={(event) => { setHotelCheckIn(event.target.value); if (hotelCheckOut <= event.target.value) { const next = new Date(`${event.target.value}T00:00:00`); next.setDate(next.getDate() + 1); setHotelCheckOut(localDateString(next)); } }} /></label>
            <label className="form-label">Check-out<input required type="date" min={hotelCheckIn} value={hotelCheckOut} onChange={(event) => setHotelCheckOut(event.target.value)} /></label>
            <label className="form-label">Guests<select required min="1" max="6" value={hotelGuests} onChange={(event) => setHotelGuests(event.target.value)}>{[1, 2, 3, 4, 5, 6].map((guestCount) => <option key={guestCount} value={guestCount}>{guestCount}</option>)}</select></label>
            <label className="form-label">Lead guest name<input required minLength="2" maxLength="100" value={hotelGuestName} onChange={(event) => setHotelGuestName(event.target.value)} /></label>
            <label className="form-label hotel-email-field">Email address<input required type="email" maxLength="254" value={hotelEmail} onChange={(event) => setHotelEmail(event.target.value)} /></label>
          </div>
          {hotelError && <div className="cancellation-error" role="alert">{hotelError}</div>}
          <p className="hotel-simulation-note">Sample reservation only. Estimated price: {money(selectedHotel.nightlyRate)} per night; payment is simulated.</p>
          <button className="confirm-button" type="submit" disabled={hotelSubmitting}><Check size={16} />{hotelSubmitting ? 'Booking…' : 'Confirm hotel booking'}</button>
        </form>
      </div>}
    </div>
  );
}

export default App;
