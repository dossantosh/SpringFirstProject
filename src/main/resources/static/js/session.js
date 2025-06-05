let inactivityTimeout;
const MAX_INACTIVITY_MINUTES = 5;

const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

const resetTimer = () => {
    clearTimeout(inactivityTimeout);
    inactivityTimeout = setTimeout(logoutUser, MAX_INACTIVITY_MINUTES * 60 * 1000);
};


const logoutUser = () => {
    fetch('/logout-inactive', {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken
        }
    })
    .then(() => window.location.href = '/login?timeout')
    .catch(err => console.error('Error al cerrar sesión por inactividad', err));
};

// Eventos que reinician el temporizador
['mousemove', 'keydown', 'wheel', 'click', 'scroll'].forEach(event => {
    document.addEventListener(event, resetTimer, false);
});

// Iniciar temporizador al cargar
resetTimer();