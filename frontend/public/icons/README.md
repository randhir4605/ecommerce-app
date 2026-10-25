# C-Store Homepage SVG Icons

A cohesive set of 23 standalone icons plus a logo for the C-Store customer-facing ecommerce homepage. All icons are SVG vector assets, so they stay sharp at any size.

## Files
- `c-store-logo.svg`: blue cart mark and C-Store wordmark
- Header/UI: `search.svg`, `shopping-cart.svg`, `user.svg`, `bell.svg`, `menu.svg`, `close.svg`, `arrow-right.svg`, `chevron-left.svg`
- Categories: `laptop.svg`, `smartphone.svg`, `shirt.svg`, `home.svg`, `beauty.svg`, `sports.svg`, `toys.svg`, `book-open.svg`
- Product/extra: `headphones.svg`, `smartwatch.svg`, `running-shoe.svg`, `backpack.svg`, `discount-tag.svg`, `star.svg`, `heart.svg`
- `preview.html`: open in a browser to preview all icons

## React usage
1. Copy the SVG files into `public/icons/` in your React project.
2. Use an icon, for example:

```jsx
<img src="/icons/search.svg" alt="" className="header-icon" />
```

```css
.header-icon { width: 1.4rem; height: 1.4rem; }
```

Each icon file includes a default stroke color so it works immediately through an ordinary `<img>` tag. To recolor an icon, edit its `stroke` color in the SVG file. The logo uses the C-Store brand colors directly.
