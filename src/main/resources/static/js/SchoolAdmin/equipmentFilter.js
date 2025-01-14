// Equipment table management
class TableManager
{
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
            <svg xmlns="http://www.w3.org/2000/svg" 
                 width="80" 
                 height="80" 
                 viewBox="0 0 24 24" 
                 fill="none" 
                 stroke="#adb5bd" 
                 stroke-width="1" 
                 stroke-linecap="round" 
                 stroke-linejoin="round" 
                 style="margin-bottom: 1rem;">
                <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                <path d="M12 3l8 4.5l0 9l-8 4.5l-8 -4.5l0 -9l8 -4.5" />
                <path d="M12 12l8 -4.5" />
                <path d="M12 12l0 9" />
                <path d="M12 12l-8 -4.5" />
            </svg>
            <div style="color: #6c757d; font-size: 1.1rem; margin-bottom: 0.5rem;">
                Barang Tidak Dijumpai
            </div>
            <div style="color: #adb5bd; font-size: 0.9rem;">
               Pastikan anda menaip nama yang betul
            </div>
        `;

        this.noDataContainer.innerHTML = noDataContent;
        this.table.parentNode.insertBefore(this.noDataContainer, this.table.nextSibling);
    }

    initializeControls() {
        this.searchInput = document.getElementById('searchEquipment');
        this.entriesSelect = document.getElementById('entries');
        this.statusFilter = document.getElementById('equipmentStatusDropdown');
        this.statusOptions = document.querySelectorAll('.dropdown-menu .dropdown-item');
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

        // Status filter
        this.statusOptions.forEach(option => {
            option.addEventListener('click', (e) => {
                e.preventDefault();
                const status = e.target.textContent;
                this.currentPage = 1;
                this.filterByStatus(status);
            });
        });
    }

    filterRows() {
        const searchTerm = this.searchInput.value.toLowerCase();
        this.filteredRows = this.rows.filter(row => {
            const itemName = row.children[1].textContent.toLowerCase();
            const itemType = row.children[2].textContent.toLowerCase();
            return itemName.includes(searchTerm) || itemType.includes(searchTerm);
        });
        this.updateTable();
    }

    filterByStatus(status) {
        if (status === 'Semua Status') {
            this.filteredRows = [...this.rows];
        } else {
            this.filteredRows = this.rows.filter(row => {
                const itemStatus = row.children[5].textContent;
                return itemStatus === status;
            });
        }
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
            const equipmentId = row.querySelector('.hover-edit').getAttribute('data-id');
            if (editButton) {
                editButton.setAttribute('data-id', equipmentId);
            }
            if (deleteButton) {
                deleteButton.setAttribute('data-id', equipmentId);
            }

            this.tbody.appendChild(newRow);
        });

        this.updatePagination(totalPages);
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
            const equipmentId = row.querySelector('.hover-edit').getAttribute('data-id');
            if (editButton) {
                editButton.setAttribute('data-id', equipmentId);
            }
            if (deleteButton) {
                deleteButton.setAttribute('data-id', equipmentId);
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
