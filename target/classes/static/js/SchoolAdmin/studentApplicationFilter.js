// TableManager class for handling table filters and pagination
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
        // Create a container for when no data matches the filter
        this.noDataContainer = document.createElement('div');
        this.noDataContainer.style.cssText = `
            display: none;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 3rem;
            text-align: center;
        `;

        const noDataContent = `
            <svg  xmlns="http://www.w3.org/2000/svg"    width="80" height="80"  viewBox="0 0 24 24"  fill="none"  stroke="#adb5bd"   
               stroke-width="1" stroke-linecap="round" 
                 stroke-linejoin="round" 
                 style="margin-bottom: 1rem;">
            ><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M9 5h-2a2 2 0 0 0 -2 2v12a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-12a2 2 0 0 0 -2 -2h-2" /><path d="M9 3m0 2a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v0a2 2 0 0 1 -2 2h-2a2 2 0 0 1 -2 -2z" /><path d="M9 12h6" /><path d="M9 16h6" /></svg>
            <div style="color: #6c757d; font-size: 1.1rem; margin-bottom: 0.5rem;">
                Tiada Pelajar dijumpai
            </div>
            <div style="color: #adb5bd; font-size: 0.9rem;">
                Pastikan anda menaip nama yang betul atau pastikan pelajar telah memohon sebelum ini.           
            </div>
        `;

        this.noDataContainer.innerHTML = noDataContent;
        this.table.parentNode.insertBefore(this.noDataContainer, this.table.nextSibling);
    }

    initializeControls() {
        this.searchInput = document.getElementById('studentName');
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
            const studentName = row.children[1].textContent.toLowerCase();
            return studentName.includes(searchTerm);
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
