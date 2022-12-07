/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: "class",
  important: true,
  content: ["./public/**/*.{html,js}"],
  theme: {
    extend: {
      colors: {
        "primary-color": "#FFD333",
        "secondary-color": "#",
        "text-color": "#",
        "background-color": "#",
        "border-color": "",
      },
      screens: {
        mobile: { max: "739px" },
        // => @media (max-width: 739px) { ... }

        tablet: { min: "740px", max: "1023px" },
        // => @media (min-width: 740px and max-width: 1023px) { ... }

        pc: { min: "1024px" },
        // => @media (min-width: 1024px) { ... }
      },
      keyframes: {
        // Animation hiện lên từ từ
        fadeIn: {
          "0%": { opacity: "0" },
          "100%": { opacity: "1" },
        },
        // Animation trượt từ trên xuống
        slideAndFade: {
          "0%": { opacity: "0", transform: "translateY(-20.25rem)" },
          "100%": { opacity: "1", transform: "translateY(0)" },
        },
        // Animation chièu dài thẻ lớn dần
        growUp: {
          "0%": { "max-height": "0px" },
          "100%": { "max-height": "1000px" },
        },
        // Animation chièu dài thẻ giảm dần
        growDown: {
          "0%": { "max-height": "400px" },
          "100%": { "max-height": "0px" },
        },
        fadeOut: {
          // Mờ dần rồi bién mất
          "0%": { opacity: "1" },
          "100%": { opacity: "0" },
        },
      },
      animations: {},
    },
  },
  plugins: [
    require("@tailwindcss/line-clamp"),
    // ...
  ],
};
