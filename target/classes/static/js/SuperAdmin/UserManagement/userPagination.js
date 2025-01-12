const searchInput = document.querySelector('.floating-input');
const roleDropdown = document.querySelector('#versiDropdown');
const entriesSelect = document.querySelector('#entries');
const tableBody = document.querySelector('tbody');
const paginationContainer = document.querySelector('.pagination');
const baseUrl = /*[[@{/}]]*/ '';

// Store original table rows for reset
const originalRows = Array.from(tableBody.querySelectorAll('tr'));
let filteredRows = [...originalRows];

// Function to attach event listeners to buttons
function attachButtonListeners(row) {
    // View button listener
    const viewButton = row.querySelector('.hover-view');
    if (viewButton) {
        viewButton.addEventListener('click', async function() {
            const userId = this.getAttribute('data-id');
            try {
                const response = await fetch(baseUrl + `Pengguna/ajax/${userId}`, {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    credentials: 'same-origin'
                });

                const data = await response.json();
                const user = data.user;

                document.getElementById('viewFullName').value = user.name;
                document.getElementById('viewEmail').value = user.emailAddress;
                document.getElementById('viewUserTypeSelect').value = user.role.name;

                const viewDaerahGroup = document.getElementById('viewDaerahGroup');
                const viewSchoolGroup = document.getElementById('viewSchoolGroup');

                viewDaerahGroup.style.display = 'none';
                viewSchoolGroup.style.display = 'none';

                if (user.role.rolename.toLowerCase() === 'ppdadmin') {
                    viewDaerahGroup.style.display = 'block';
                    document.getElementById('viewDistrict').value = user.district;
                }

                if (user.role.rolename.toLowerCase() === 'schooladmin') {
                    viewDaerahGroup.style.display = 'block';
                    viewSchoolGroup.style.display = 'block';
                    document.getElementById('viewDistrict').value = user.district;
                    if (user.school) {
                        document.getElementById('viewSchool').value = user.school.name;
                    }
                }
            } catch (error) {
                console.error('Error in view button click handler:', error);
            }
        });
    }

    // Edit button listener
    const editButton = row.querySelector('.hover-edit');
    if (editButton) {
        editButton.addEventListener('click', async function() {
            const userId = this.getAttribute('data-id');
            try {
                const response = await fetch(baseUrl + `Pengguna/ajax/${userId}`, {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    credentials: 'same-origin'
                });

                const data = await response.json();
                const user = data.user;

                document.getElementById('editFullName').value = user.name;
                document.getElementById('editEmail').value = user.emailAddress;
                document.getElementById('editUserId').value = user.id;
            } catch (error) {
                console.error('Error in edit button click handler:', error);
            }
        });
    }

    // Delete button listener
    const deleteButton = row.querySelector('.hover-delete');
    if (deleteButton) {
        deleteButton.addEventListener('click', function() {
            const userId = this.getAttribute('data-id');
            document.getElementById('userIdToDelete').value = userId;
        });
    }
}

// Search functionality
searchInput.addEventListener('input', function(e) {
    const searchTerm = e.target.value.toLowerCase();
    filterAndDisplayRows();
});

// Role filter functionality
document.querySelectorAll('.dropdown-item').forEach(item => {
    item.addEventListener('click', function(e) {
        e.preventDefault();
        const selectedRole = this.textContent;
        roleDropdown.querySelector('span').textContent = selectedRole;
        filterAndDisplayRows();
    });
});

// Entries per page functionality
entriesSelect.addEventListener('change', function() {
    currentPage = 1;
    updateTable();
});

function filterAndDisplayRows() {
    const searchTerm = searchInput.value.toLowerCase();
    const selectedRole = roleDropdown.querySelector('span').textContent.trim();

    filteredRows = originalRows.filter(row => {
        const name = row.querySelector('td:nth-child(2)').textContent.toLowerCase();
        const email = row.querySelector('td:nth-child(3)').textContent.toLowerCase();
        const role = row.querySelector('.badge').textContent.trim();

        const matchesSearch = name.includes(searchTerm) || email.includes(searchTerm);
        const matchesRole = selectedRole === 'Jenis Pengguna' || selectedRole === 'Semua Pengguna'
            || role === selectedRole;

        return matchesSearch && matchesRole;
    });

    currentPage = 1;
    updateTable();
}

let currentPage = 1;

function updateTable() {
    const entriesPerPage = parseInt(entriesSelect.value);
    const startIndex = (currentPage - 1) * entriesPerPage;
    const endIndex = startIndex + entriesPerPage;
    const paginatedRows = filteredRows.slice(startIndex, endIndex);

    tableBody.innerHTML = '';

    if (paginatedRows.length === 0) {
        // Add empty state row
        const emptyRow = document.createElement('tr');
        emptyRow.innerHTML = `
            <td colspan="6" class="text-center py-4">
                <div class="d-flex flex-column align-items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-muted mb-3">
                        <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                        <circle cx="9" cy="7" r="4"></circle>
                        <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
                        <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
                    </svg>
                    <p class="text-muted">Tiada data untuk ditunjukkan</p>
                </div>
            </td>
        `;
        tableBody.appendChild(emptyRow);

        paginationContainer.innerHTML = '';
        return;
    }else {

    paginatedRows.forEach((row, index) => {
        const newRow = row.cloneNode(true);
        const numberCell = newRow.querySelector('td:first-child');
        numberCell.textContent = startIndex + index + 1;

        // Attach event listeners to the new row's buttons
        attachButtonListeners(newRow);

        tableBody.appendChild(newRow);
    });
        updatePagination();
    }
}

function updatePagination() {
    const entriesPerPage = parseInt(entriesSelect.value);
    const totalPages = Math.ceil(filteredRows.length / entriesPerPage);

    let paginationHTML = `
        <li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage - 1}" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
    `;

    // Show limited page numbers with ellipsis
    const maxVisiblePages = 5;
    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

    // Adjust start page if we're near the end
    startPage = Math.max(1, Math.min(startPage, totalPages - maxVisiblePages + 1));

    if (startPage > 1) {
        paginationHTML += `
            <li class="page-item">
                <a class="page-link" href="#" data-page="1">1</a>
            </li>
            ${startPage > 2 ? '<li class="page-item disabled"><span class="page-link">...</span></li>' : ''}
        `;
    }

    for (let i = startPage; i <= endPage; i++) {
        paginationHTML += `
            <li class="page-item ${currentPage === i ? 'active' : ''}">
                <a class="page-link" href="#" data-page="${i}">${i}</a>
            </li>
        `;
    }

    if (endPage < totalPages) {
        paginationHTML += `
            ${endPage < totalPages - 1 ? '<li class="page-item disabled"><span class="page-link">...</span></li>' : ''}
            <li class="page-item">
                <a class="page-link" href="#" data-page="${totalPages}">${totalPages}</a>
            </li>
        `;
    }

    paginationHTML += `
        <li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage + 1}" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    `;

    paginationContainer.innerHTML = paginationHTML;

    // Add event listeners to pagination buttons
    paginationContainer.querySelectorAll('.page-link').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const newPage = parseInt(this.dataset.page);
            if (!isNaN(newPage) && newPage >= 1 && newPage <= totalPages) {
                currentPage = newPage;
                updateTable();
            }
        });
    });
}

// Initial table setup
updateTable();

const modals = ['addUserModal', 'viewUserModal', 'editUserModal'];
modals.forEach(modalId => {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.addEventListener('hidden.bs.modal', function() {
            resetModalData(modalId);
        });
    }
});

function resetModalData(modalId) {
    const modal = document.getElementById(modalId);
    const inputs = modal.querySelectorAll('input');
    inputs.forEach(input => input.value = '');

    if (modalId === 'addUserModal') {
        const daerahGroupAdd = document.getElementById('daerahGroup');
        const schoolGroupAdd = document.getElementById('schoolGroup');
        if (daerahGroupAdd) daerahGroupAdd.style.display = 'none';
        if (schoolGroupAdd) schoolGroupAdd.style.display = 'none';
    }
}