class SchoolTableManager {
    constructor() {
        this.table = document.querySelector('.table-school');
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
                <svg  xmlns="http://www.w3.org/2000/svg"  width="80"  height="80"  viewBox="0 0 24 24"  fill="none"  stroke="#adb5bd"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-building-bank"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M3 21l18 0" /><path d="M3 10l18 0" /><path d="M5 6l7 -3l7 3" /><path d="M4 10l0 11" /><path d="M20 10l0 11" /><path d="M8 14l0 3" /><path d="M12 14l0 3" /><path d="M16 14l0 3" /></svg>
            <div style="color: #6c757d; font-size: 1.1rem; margin-bottom: 0.5rem;">
                Sekolah Tidak Dijumpai
            </div>
            <div style="color: #adb5bd; font-size: 0.9rem;">
               Pastikan anda menaip nama yang betul
            </div>
        `;

        this.noDataContainer.innerHTML = noDataContent;
        this.table.parentNode.insertBefore(this.noDataContainer, this.table.nextSibling);
    }

    initializeControls() {
        this.searchInput = document.getElementById('searchSchool');
        this.entriesSelect = document.getElementById('entries');
        this.versiDropdown = document.getElementById('versiDropdown');
        this.daerahDropdown = document.getElementById('daerahDropdown');
        this.versiOptions = document.querySelectorAll('[aria-labelledby="versiDropdown"] .dropdown-item');
        this.daerahOptions = document.querySelectorAll('[aria-labelledby="daerahDropdown"] .dropdown-item');
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

        // Version filter
        this.versiOptions.forEach(option => {
            option.addEventListener('click', (e) => {
                e.preventDefault();
                const version = e.target.textContent;
                this.versiDropdown.querySelector('span').textContent = version;
                this.currentPage = 1;
                this.filterRows();
            });
        });

        // District filter
        this.daerahOptions.forEach(option => {
            option.addEventListener('click', (e) => {
                e.preventDefault();
                const district = e.target.textContent;
                this.daerahDropdown.querySelector('span').textContent = district;
                this.currentPage = 1;
                this.filterRows();
            });
        });
    }

    filterRows() {
        const searchTerm = this.searchInput.value.toLowerCase();
        const selectedVersion = this.versiDropdown.querySelector('span').textContent;
        const selectedDistrict = this.daerahDropdown.querySelector('span').textContent;

        this.filteredRows = this.rows.filter(row => {
            const schoolCode = row.children[0].textContent.toLowerCase();
            const district = row.children[1].textContent.toLowerCase();
            const schoolName = row.children[2].textContent.toLowerCase();
            const officerName = row.children[3].textContent.toLowerCase();
            const version = row.children[4].textContent;

            const matchesSearch = schoolCode.includes(searchTerm) ||
                district.includes(searchTerm) ||
                schoolName.includes(searchTerm) ||
                officerName.includes(searchTerm);

            const matchesVersion = selectedVersion === 'Versi' ||
                selectedVersion === 'Semua Versi' ||
                version === selectedVersion;

            const matchesDistrict = selectedDistrict === 'Daerah' ||
                selectedDistrict === 'Semua Daerah' ||
                district.includes(selectedDistrict.toLowerCase());

            return matchesSearch && matchesVersion && matchesDistrict;
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
        this.filteredRows.slice(startIndex, endIndex).forEach(row => {
            const newRow = row.cloneNode(true);
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

// Initialize the table manager when the document is ready
document.addEventListener('DOMContentLoaded', () => {
    new SchoolTableManager();
});