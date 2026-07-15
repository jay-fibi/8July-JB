const header = document.querySelector('.site-header');
const menuButton = document.querySelector('.menu-toggle');
const navigation = document.querySelector('.main-nav');
const bookingForm = document.querySelector('#booking-form');
const packageSelect = document.querySelector('#package-select');
const bookingDate = document.querySelector('#booking-date');
const formMessage = document.querySelector('.form-message');
const toast = document.querySelector('.toast');

function updateHeader() {
  header.classList.toggle('scrolled', window.scrollY > 12);
}

function closeMenu() {
  navigation.classList.remove('open');
  document.body.classList.remove('menu-open');
  menuButton.setAttribute('aria-expanded', 'false');
  menuButton.setAttribute('aria-label', 'Open menu');
}

menuButton.addEventListener('click', () => {
  const isOpen = navigation.classList.toggle('open');
  document.body.classList.toggle('menu-open', isOpen);
  menuButton.setAttribute('aria-expanded', String(isOpen));
  menuButton.setAttribute('aria-label', isOpen ? 'Close menu' : 'Open menu');
});

navigation.addEventListener('click', event => {
  if (event.target.matches('a')) closeMenu();
});

window.addEventListener('scroll', updateHeader, { passive: true });
window.addEventListener('resize', () => {
  if (window.innerWidth > 900) closeMenu();
});
updateHeader();

const today = new Date();
const localToday = new Date(today.getTime() - today.getTimezoneOffset() * 60000).toISOString().split('T')[0];
bookingDate.min = localToday;

const day = today.getDay();
const hour = today.getHours();
const openHour = day === 0 ? 9 : 8;
const closeHour = day === 0 ? 17 : 19;
const hoursStatus = document.querySelector('#hours-status');
if (hour < openHour || hour >= closeHour) {
  document.querySelector('.status-pill strong').textContent = 'Currently closed';
  document.querySelector('.status-pill > span').style.background = '#ff8f78';
  hoursStatus.textContent = day === 0 ? 'Opens 9:00 AM' : 'Opens 8:00 AM';
}

function showToast(message) {
  toast.textContent = message;
  toast.classList.add('show');
  clearTimeout(showToast.timeout);
  showToast.timeout = setTimeout(() => toast.classList.remove('show'), 2600);
}

document.querySelectorAll('.package-button').forEach(button => {
  button.addEventListener('click', () => {
    packageSelect.value = button.dataset.package;
    document.querySelector('#booking').scrollIntoView({ behavior: 'smooth' });
    setTimeout(() => bookingForm.querySelector('[name="name"]').focus({ preventScroll: true }), 600);
  });
});

bookingForm.addEventListener('submit', event => {
  event.preventDefault();
  const fields = [...bookingForm.querySelectorAll('input, select')];
  fields.forEach(field => field.classList.toggle('invalid', !field.checkValidity()));
  const firstInvalid = fields.find(field => !field.checkValidity());

  if (firstInvalid) {
    formMessage.textContent = 'Please complete all fields so we can reserve your spot.';
    formMessage.className = 'form-message show error';
    firstInvalid.focus();
    return;
  }

  const data = new FormData(bookingForm);
  const readableDate = new Date(`${data.get('date')}T12:00:00`).toLocaleDateString(undefined, { month: 'long', day: 'numeric' });
  formMessage.textContent = `Thanks, ${data.get('name')}! Your ${data.get('package')} wash is requested for ${readableDate} at ${data.get('time')}.`;
  formMessage.className = 'form-message show';
  fields.forEach(field => field.classList.remove('invalid'));
  showToast('Appointment request received!');
  bookingForm.reset();
});

const reviews = [
  { quote: "I honestly didn't recognize my car when I picked it up. Every tiny detail was spotless, and the team was incredibly friendly. Best car wash in town—no contest.", name: 'Maya Chen', car: 'Signature Wash · Honda CR-V', avatar: 'MC' },
  { quote: 'Fast, careful, and genuinely friendly. The spray wax still beads water weeks later, and the interior looks better than it has in years.', name: 'Jordan Lee', car: 'Signature Wash · Tesla Model 3', avatar: 'JL' },
  { quote: 'The Showroom detail was worth every penny. They got out stains I thought were permanent and made the paint look incredible.', name: 'Alex Rivera', car: 'Showroom Detail · Ford Bronco', avatar: 'AR' }
];
let reviewIndex = 0;

function renderReview() {
  const review = reviews[reviewIndex];
  document.querySelector('#review-quote').textContent = review.quote;
  document.querySelector('#review-name').textContent = review.name;
  document.querySelector('#review-car').textContent = review.car;
  document.querySelector('#review-avatar').textContent = review.avatar;
}

document.querySelector('#prev-review').addEventListener('click', () => {
  reviewIndex = (reviewIndex - 1 + reviews.length) % reviews.length;
  renderReview();
});
document.querySelector('#next-review').addEventListener('click', () => {
  reviewIndex = (reviewIndex + 1) % reviews.length;
  renderReview();
});

document.querySelector('.newsletter').addEventListener('submit', event => {
  event.preventDefault();
  showToast('You’re on the list. Welcome to DripDrop!');
  event.currentTarget.reset();
});

document.querySelectorAll('.faq-list details').forEach(item => {
  item.addEventListener('toggle', () => {
    if (!item.open) return;
    document.querySelectorAll('.faq-list details').forEach(other => {
      if (other !== item) other.open = false;
    });
  });
});

const observer = new IntersectionObserver(entries => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      entry.target.classList.add('visible');
      observer.unobserve(entry.target);
    }
  });
}, { threshold: 0.12 });
document.querySelectorAll('.reveal').forEach(element => observer.observe(element));
document.querySelector('#year').textContent = new Date().getFullYear();