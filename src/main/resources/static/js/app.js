// Global JavaScript for JobBoard Application

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        if (alert.classList.contains('alert-success') || alert.classList.contains('alert-info')) {
            setTimeout(function() {
                alert.style.transition = 'opacity 0.5s';
                alert.style.opacity = '0';
                setTimeout(function() {
                    alert.remove();
                }, 500);
            }, 5000);
        }
    });

    // Form validation enhancement
    const forms = document.querySelectorAll('form');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(e) {
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Enhanced search functionality
    const searchInput = document.getElementById('searchApplications');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(function(e) {
            const searchTerm = e.target.value.toLowerCase().trim();
            const rows = document.querySelectorAll('tbody tr');
            let visibleCount = 0;
            
            rows.forEach(function(row) {
                const text = row.textContent.toLowerCase();
                const shouldShow = text.includes(searchTerm);
                row.style.display = shouldShow ? '' : 'none';
                if (shouldShow) visibleCount++;
            });
            
            // Show/hide "no results" message
            let noResultsRow = document.querySelector('.no-results-row');
            if (visibleCount === 0 && searchTerm !== '') {
                if (!noResultsRow) {
                    const tbody = document.querySelector('tbody');
                    const colCount = document.querySelectorAll('thead th').length;
                    noResultsRow = document.createElement('tr');
                    noResultsRow.className = 'no-results-row';
                    noResultsRow.innerHTML = `
                        <td colspan="${colCount}" class="text-center py-4 text-muted">
                            <i class="fas fa-search mb-2"></i><br>
                            No results found for "${searchTerm}"
                        </td>
                    `;
                    tbody.appendChild(noResultsRow);
                }
                noResultsRow.style.display = '';
            } else if (noResultsRow) {
                noResultsRow.style.display = 'none';
            }
        }, 300));
    }

    // Smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(function(anchor) {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Animation on scroll
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(function(entry) {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
            }
        });
    }, observerOptions);

    // Observe job cards for animation
    document.querySelectorAll('.job-card').forEach(function(card) {
        observer.observe(card);
    });

    // Confirm dialog for delete actions
    document.querySelectorAll('[data-confirm]').forEach(function(element) {
        element.addEventListener('click', function(e) {
            const message = this.getAttribute('data-confirm');
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });

    // Auto-focus on modal inputs
    document.querySelectorAll('.modal').forEach(function(modal) {
        modal.addEventListener('shown.bs.modal', function() {
            const firstInput = this.querySelector('input, select, textarea');
            if (firstInput) {
                firstInput.focus();
            }
        });
    });
});

// Utility function: Debounce
function debounce(func, wait, immediate) {
    let timeout;
    return function executedFunction() {
        const context = this;
        const args = arguments;
        const later = function() {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        const callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    };
}

// Utility function: Format date
function formatDate(dateString) {
    const date = new Date(dateString);
    const options = { 
        year: 'numeric', 
        month: 'short', 
        day: 'numeric' 
    };
    return date.toLocaleDateString('en-US', options);
}

// Loading state management
function showLoading(element) {
    const originalText = element.innerHTML;
    element.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Loading...';
    element.disabled = true;
    element.setAttribute('data-original-text', originalText);
}

function hideLoading(element) {
    const originalText = element.getAttribute('data-original-text');
    if (originalText) {
        element.innerHTML = originalText;
        element.disabled = false;
        element.removeAttribute('data-original-text');
    }
}

// Toast notification system
function showToast(message, type = 'success') {
    const toastContainer = document.querySelector('.toast-container') || createToastContainer();
    
    const toast = document.createElement('div');
    toast.className = `toast align-items-center text-white bg-${type} border-0`;
    toast.setAttribute('role', 'alert');
    toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">${message}</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" 
                    data-bs-dismiss="toast"></button>
        </div>
    `;
    
    toastContainer.appendChild(toast);
    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();
    
    // Remove toast element after it's hidden
    toast.addEventListener('hidden.bs.toast', function() {
        toast.remove();
    });
}

function createToastContainer() {
    const container = document.createElement('div');
    container.className = 'toast-container position-fixed top-0 end-0 p-3';
    container.style.zIndex = '1055';
    document.body.appendChild(container);
    return container;
}