const headerContainer = document.getElementById('header');
if (!headerContainer) {
    const header = document.createElement('header');
    fetch('../partials/header-cliente.html')
        .then(response => response.text())
        .then(data => {
            header.innerHTML = data;
            document.body.insertBefore(header, document.body.firstChild);
        })
        .catch(error => console.error(error));
}