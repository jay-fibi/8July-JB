const body = document.body;
const themeToggle = document.querySelector('.theme-toggle');
const menuToggle = document.querySelector('.menu-toggle');
const mainNav = document.querySelector('.main-nav');
const cartButton = document.querySelector('.cart-button');
const cartDrawer = document.querySelector('.cart-drawer');
const cartClose = document.querySelector('.cart-close');
const overlay = document.querySelector('.overlay');
const cartItems = document.querySelector('.cart-items');
const cartCount = document.querySelector('.cart-count');
const cartTotal = document.querySelector('.cart-total');
const toast = document.querySelector('.toast');
const cart = [];

function setTheme(theme) {
  const isDark = theme === 'dark';
  body.classList.toggle('dark', isDark);
  themeToggle.textContent = isDark ? '☀️' : '🌙';
  themeToggle.setAttribute('aria-label', `Switch to ${isDark ? 'light' : 'dark'} theme`);
  localStorage.setItem('freshcart-theme', theme);
}

setTheme(localStorage.getItem('freshcart-theme') || 'light');
themeToggle.addEventListener('click', () => setTheme(body.classList.contains('dark') ? 'light' : 'dark'));

menuToggle.addEventListener('click', () => {
  const isOpen = mainNav.classList.toggle('open');
  menuToggle.setAttribute('aria-expanded', String(isOpen));
  menuToggle.textContent = isOpen ? '×' : '☰';
});

mainNav.addEventListener('click', event => {
  if (event.target.matches('a')) {
    mainNav.classList.remove('open');
    menuToggle.setAttribute('aria-expanded', 'false');
    menuToggle.textContent = '☰';
  }
});

function openCart() {
  cartDrawer.classList.add('open');
  cartDrawer.setAttribute('aria-hidden', 'false');
  overlay.hidden = false;
  cartClose.focus();
}

function closeCart() {
  cartDrawer.classList.remove('open');
  cartDrawer.setAttribute('aria-hidden', 'true');
  overlay.hidden = true;
  cartButton.focus();
}

cartButton.addEventListener('click', openCart);
cartClose.addEventListener('click', closeCart);
overlay.addEventListener('click', closeCart);
document.addEventListener('keydown', event => {
  if (event.key === 'Escape' && cartDrawer.classList.contains('open')) closeCart();
});

function showToast(message) {
  toast.textContent = message;
  toast.classList.add('show');
  clearTimeout(showToast.timeout);
  showToast.timeout = setTimeout(() => toast.classList.remove('show'), 2200);
}

function productIcon(name) {
  const icons = { 'Organic Red Apples': '🍎', 'Sweet Bananas': '🍌', 'Fresh Broccoli': '🥦', 'Crunchy Carrots': '🥕', 'Whole Milk': '🥛', 'Free-Range Eggs': '🥚', 'Artisan Baguette': '🥖', 'Butter Croissants': '🥐' };
  return icons[name] || '🛍️';
}

function renderCart() {
  const quantity = cart.reduce((total, item) => total + item.quantity, 0);
  const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
  cartCount.textContent = quantity;
  cartTotal.textContent = `$${total.toFixed(2)}`;

  if (!cart.length) {
    cartItems.innerHTML = '<div class="cart-empty"><span>🧺</span><h3>Your cart is empty</h3><p>Add some fresh favorites to get started.</p></div>';
    return;
  }

  cartItems.innerHTML = cart.map((item, index) => `
    <div class="cart-item">
      <span class="cart-item-icon">${productIcon(item.name)}</span>
      <div><strong>${item.name}</strong><small>${item.quantity} × $${item.price.toFixed(2)}</small></div>
      <button class="remove-item" type="button" data-index="${index}" aria-label="Remove ${item.name}">Remove</button>
    </div>`).join('');
}

document.querySelectorAll('.add-button').forEach(button => {
  button.addEventListener('click', () => {
    const name = button.dataset.name;
    const price = Number(button.dataset.price);
    const existing = cart.find(item => item.name === name);
    if (existing) existing.quantity += 1;
    else cart.push({ name, price, quantity: 1 });
    renderCart();
    showToast(`${name} added to your cart`);
  });
});

cartItems.addEventListener('click', event => {
  const removeButton = event.target.closest('.remove-item');
  if (!removeButton) return;
  cart.splice(Number(removeButton.dataset.index), 1);
  renderCart();
});

document.querySelectorAll('.category-card').forEach(button => {
  button.addEventListener('click', () => {
    document.querySelectorAll('.category-card').forEach(item => item.classList.remove('active'));
    button.classList.add('active');
    const selected = button.dataset.category;
    document.querySelectorAll('.product-card').forEach(product => {
      product.hidden = selected !== 'all' && product.dataset.category !== selected;
    });
    document.querySelector('#products').scrollIntoView({ behavior: 'smooth' });
  });
});

document.querySelector('.newsletter-form').addEventListener('submit', event => {
  event.preventDefault();
  showToast('Thanks for joining the FreshCart list!');
  event.currentTarget.reset();
});

document.querySelector('.checkout-button').addEventListener('click', () => {
  showToast(cart.length ? 'Checkout is ready for backend integration.' : 'Your cart is empty.');
});

document.querySelector('#year').textContent = new Date().getFullYear();