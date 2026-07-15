# DripDrop Car Wash

A polished, responsive one-page website for a local car-wash and detailing business. Built with semantic HTML, modern CSS, and vanilla JavaScript—no build step or dependencies required.

## Features

- Responsive navigation and mobile layout
- Custom CSS car illustration with no image dependencies
- Service packages with one-click booking selection
- Client-side booking form validation and confirmation
- Dynamic opening-hours status
- Interactive testimonial carousel and FAQ accordion
- Newsletter feedback, scroll reveals, and reduced-motion support
- Accessible labels, keyboard navigation, and semantic page structure

## Run locally

Open `index.html` directly in a browser, or from this directory run:

```sh
python -m http.server 8000
```

Then visit `http://localhost:8000`.

## Production note

The booking and newsletter forms currently provide client-side demonstrations. Connect their submit handlers in `script.js` to an email, CRM, or scheduling API before accepting real customer submissions.