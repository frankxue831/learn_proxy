// Learn Proxy - Main JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            document.querySelector(this.getAttribute('href')).scrollIntoView({
                behavior: 'smooth'
            });
        });
    });

    // Add copy code button functionality
    const codeBlocks = document.querySelectorAll('pre code');
    codeBlocks.forEach(block => {
        const button = document.createElement('button');
        button.className = 'copy-code-btn';
        button.textContent = 'Copy';
        button.onclick = () => {
            navigator.clipboard.writeText(block.textContent);
            button.textContent = 'Copied!';
            setTimeout(() => button.textContent = 'Copy', 2000);
        };
        block.parentNode.appendChild(button);
    });

    // Mobile menu toggle (if needed)
    const navToggle = document.querySelector('.nav-toggle');
    const nav = document.querySelector('.site-nav');
    
    if (navToggle) {
        navToggle.addEventListener('click', () => {
            nav.classList.toggle('active');
        });
    }
});