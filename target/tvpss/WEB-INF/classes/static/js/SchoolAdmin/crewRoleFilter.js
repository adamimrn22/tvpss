// Role table management
class TableManager {
    constructor() {
        this.table = document.querySelector('table');
        this.tbody = this.table.querySelector('tbody');
        this.rows = Array.from(this.tbody.querySelectorAll('tr'));
        this.itemsPerPage = parseInt(document.getElementById('entries').value);
        this.currentPage = 1;
        this.filteredRows = [...this.rows];

        this.createNoDataElement();
        this.initializeControls();
        this.initializeEventListeners();
        this.updateTable();
    }

    createNoDataElement() {
        // Create no data container
        this.noDataContainer = document.createElement('div');
        this.noDataContainer.style.cssText = `
            display: none;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 3rem;
            text-align: center;
        `;

        // Create content with larger box icon
        const noDataContent = `
<svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" fill="#adb5bd" class="bi bi-person-circle" viewBox="0 0 16 16">
  <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
  <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
</svg>
            <div style="color: #6c757d; font-size: 1.1rem; margin-bottom: 0.5rem;">
                Jawatan Tidak Dijumpai
            </div>
            <div style="color: #adb5bd; font-size: 0.9rem;">
                Pastikan anda menaip nama jawatan yang betul
            </div>
        `;

        this.noDataContainer.innerHTML = noDataContent;
        this.table.parentNode.insertBefore(this.noDataContainer, this.table.nextSibling);
    }

    initializeControls() {
        this.searchInput = document.getElementById('searchRole');
        this.entriesSelect = document.getElementById('entries');
        this.pagination = document.querySelector('.pagination');
    }

    initializeEventListeners() {
        // Search functionality
        this.searchInput.addEventListener('input', () => {
            this.currentPage = 1;
            this.filterRows();
        });

        // Entries per page
        this.entriesSelect.addEventListener('change', (e) => {
            this.itemsPerPage = parseInt(e.target.value);
            this.currentPage = 1;
            this.updateTable();
        });
    }

    filterRows() {
        const searchTerm = this.searchInput.value.toLowerCase();
        this.filteredRows = this.rows.filter(row => {
            const roleName = row.children[1].textContent.toLowerCase();
            return roleName.includes(searchTerm);
        });
        this.updateTable();
    }

    updateTable() {
        // Check if there are any filtered rows
        if (this.filteredRows.length === 0) {
            this.table.style.display = 'none';
            this.pagination.style.display = 'none';
            this.noDataContainer.style.display = 'flex';
            return;
        }

        // Show table and hide no data message if we have rows
        this.table.style.display = 'table';
        this.pagination.style.display = 'flex';
        this.noDataContainer.style.display = 'none';

        // Calculate pagination
        const totalPages = Math.ceil(this.filteredRows.length / this.itemsPerPage);
        const startIndex = (this.currentPage - 1) * this.itemsPerPage;
        const endIndex = startIndex + this.itemsPerPage;

        // Clear current rows
        this.tbody.innerHTML = '';

        // Add visible rows
        this.filteredRows.slice(startIndex, endIndex).forEach((row, index) => {
            const newRow = row.cloneNode(true);

            // Update row numbers (serial number column)
            newRow.querySelector('td').textContent = startIndex + index + 1;

            // Ensure the action buttons have correct 'data-id' attributes
            const editButton = newRow.querySelector('.hover-edit');
            const deleteButton = newRow.querySelector('.hover-delete');

            // Make sure the 'data-id' for each button is preserved
            const roleId = row.querySelector('.hover-edit').getAttribute('data-id');
            if (editButton) {
                editButton.setAttribute('data-id', roleId);
            }
            if (deleteButton) {
                deleteButton.setAttribute('data-id', roleId);
            }

            this.tbody.appendChild(newRow);
        });

        this.updatePagination(totalPages);
    }

    updatePagination(totalPages) {
        let paginationHTML = `
            <li class="page-item ${this.currentPage === 1 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${this.currentPage - 1}" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>`;

        for (let i = 1; i <= totalPages; i++) {
            paginationHTML += `
                <li class="page-item ${i === this.currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" data-page="${i}">${i}</a>
                </li>`;
        }

        paginationHTML += `
            <li class="page-item ${this.currentPage === totalPages ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${this.currentPage + 1}" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>`;

        this.pagination.innerHTML = paginationHTML;

        // Add click handlers for pagination
        this.pagination.querySelectorAll('.page-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const page = parseInt(e.target.closest('.page-link').dataset.page);
                if (page && page !== this.currentPage && page > 0 && page <= totalPages) {
                    this.currentPage = page;
                    this.updateTable();
                }
            });
        });
    }
}
