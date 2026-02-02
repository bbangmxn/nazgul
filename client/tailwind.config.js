/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        // Threads-inspired colors
        primary: {
          DEFAULT: '#000000',
          dark: '#FFFFFF',
        },
        secondary: {
          DEFAULT: '#737373',
          dark: '#A3A3A3',
        },
        background: {
          DEFAULT: '#FFFFFF',
          dark: '#101010',
        },
        surface: {
          DEFAULT: '#F5F5F5',
          dark: '#1A1A1A',
        },
        border: {
          DEFAULT: '#E5E5E5',
          dark: '#2A2A2A',
        },
        accent: {
          DEFAULT: '#0095F6',
          hover: '#0074CC',
        },
      },
      fontFamily: {
        sans: ['-apple-system', 'BlinkMacSystemFont', 'Segoe UI', 'Roboto', 'Helvetica', 'Arial', 'sans-serif'],
      },
      fontSize: {
        '2xs': '0.625rem',
      },
      animation: {
        'fade-in': 'fadeIn 0.2s ease-out',
        'slide-up': 'slideUp 0.3s ease-out',
        'scale-in': 'scaleIn 0.2s ease-out',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        slideUp: {
          '0%': { transform: 'translateY(10px)', opacity: '0' },
          '100%': { transform: 'translateY(0)', opacity: '1' },
        },
        scaleIn: {
          '0%': { transform: 'scale(0.95)', opacity: '0' },
          '100%': { transform: 'scale(1)', opacity: '1' },
        },
      },
    },
  },
  plugins: [],
}
