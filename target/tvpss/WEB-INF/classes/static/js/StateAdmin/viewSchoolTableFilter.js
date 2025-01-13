class TableManager {
    constructor(tableSelector) {
        this.table = document.querySelector(tableSelector);
        this.tbody = this.table.querySelector('tbody');
        this.originalData = [...this.tbody.querySelectorAll('tr')].map(row => ({
            element: row,
            data: [...row.querySelectorAll('td')].map(cell => cell.textContent.toLowerCase())
        }));
        this.currentPage = 1;
        this.entriesPerPage = 10;
        this.filteredData = [...this.originalData];

        this.initializeEventListeners();
    }

    initializeEventListeners() {
        // Search functionality
        const searchInput = document.getElementById('searchSchool');
        searchInput.addEventListener('input', () => {
            this.filterTable();
        });

        // Version filter
        const versionDropdown = document.getElementById('versiDropdown');
        versionDropdown.parentElement.querySelectorAll('.dropdown-item').forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                const version = e.target.textContent;
                this.filterByVersion(version);
            });
        });

        // District filter
        const districtDropdown = document.getElementById('daerahDropdown');
        districtDropdown.parentElement.querySelectorAll('.dropdown-item').forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                const district = e.target.textContent;
                this.filterByDistrict(district);
            });
        });

        // Entries per page
        const entriesSelect = document.getElementById('entries');
        entriesSelect.addEventListener('change', () => {
            this.entriesPerPage = parseInt(entriesSelect.value);
            this.currentPage = 1;
            this.updateTable();
        });

        // Pagination
        const pagination = document.querySelector('.pagination');
        pagination.addEventListener('click', (e) => {
            e.preventDefault();
            const target = e.target.closest('.page-link');
            if (!target) return;

            // Check if parent li is disabled
            if (target.closest('.page-item').classList.contains('disabled')) {
                return;
            }

            // Handle previous button
            if (target.getAttribute('aria-label') === 'Previous' ||
                target.querySelector('span')?.getAttribute('aria-label') === 'Previous') {
                this.currentPage = Math.max(1, this.currentPage - 1);
            }
            // Handle next button
            else if (target.getAttribute('aria-label') === 'Next' ||
                target.querySelector('span')?.getAttribute('aria-label') === 'Next') {
                this.currentPage = Math.min(this.getTotalPages(), this.currentPage + 1);
            }
            // Handle number buttons
            else {
                const pageNumber = parseInt(target.textContent);
                if (!isNaN(pageNumber)) {
                    this.currentPage = pageNumber;
                }
            }
            this.updateTable();
        });

        // Column sorting
        const headers = this.table.querySelectorAll('th');
        headers.forEach((header, index) => {
            header.style.cursor = 'pointer';
            header.addEventListener('click', () => this.sortByColumn(index));
        });
    }


    filterTable() {
        const searchTerm = document.getElementById('searchSchool').value.toLowerCase();
        this.filteredData = this.originalData.filter(row => {
            return row.data.some(cell => cell.includes(searchTerm));
        });
        this.currentPage = 1;
        this.updateTable();
    }

    filterByVersion(version) {
        if (version === 'Semua Versi') {
            this.filteredData = [...this.originalData];
        } else {
            this.filteredData = this.originalData.filter(row => {
                return row.data[4] === version.toLowerCase();
            });
        }
        this.currentPage = 1;
        this.updateTable();
    }

    filterByDistrict(district) {
        if(district === 'Semua Daerah'){
            this.filteredData = [...this.originalData];
        }else {
            this.filteredData = this.originalData.filter(row => {
                return row.data[1].includes(district.toLowerCase());
            });
        }
        this.currentPage = 1;
        this.updateTable();
    }

    sortByColumn(columnIndex) {
        const currentHeader = this.table.querySelectorAll('th')[columnIndex];
        const isAscending = currentHeader.getAttribute('data-sort') !== 'asc';

        // Update sort direction indicator
        this.table.querySelectorAll('th').forEach(th => th.removeAttribute('data-sort'));
        currentHeader.setAttribute('data-sort', isAscending ? 'asc' : 'desc');

        this.filteredData.sort((a, b) => {
            const valueA = a.data[columnIndex];
            const valueB = b.data[columnIndex];
            return isAscending
                ? valueA.localeCompare(valueB)
                : valueB.localeCompare(valueA);
        });

        this.updateTable();
    }

    getTotalPages() {
        return Math.ceil(this.filteredData.length / this.entriesPerPage);
    }

    updatePagination() {
        const totalPages = this.getTotalPages();
        const pagination = document.querySelector('.pagination');
        const paginationHTML = [];

        paginationHTML.push(`<li class="page-item${this.currentPage === 1 ? ' disabled' : ''}">
            <a class="page-link" href="#" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>`);

        for (let i = 1; i <= totalPages; i++) {
            paginationHTML.push(`
                <li class="page-item${i === this.currentPage ? ' active' : ''}">
                    <a class="page-link" href="#">${i}</a>
                </li>
            `);
        }

        paginationHTML.push(`<li class="page-item${this.currentPage === totalPages ? ' disabled' : ''}">
            <a class="page-link" href="#" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>`);

        pagination.innerHTML = paginationHTML.join('');
    }

    updateTable() {
        if (this.filteredData.length === 0) {
            this.tbody.innerHTML = `
                <tr>
                       <td colspan="8" class="text-center py-4">
                        <div class="d-flex flex-column align-items-center">
                          <svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-building-bank"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M3 21l18 0" /><path d="M3 10l18 0" /><path d="M5 6l7 -3l7 3" /><path d="M4 10l0 11" /><path d="M20 10l0 11" /><path d="M8 14l0 3" /><path d="M12 14l0 3" /><path d="M16 14l0 3" /></svg>
                            <p class="text-muted">Tiada data untuk ditunjukkan</p>
                        </div>
                       </td>
                </tr>
            `;
        } else {
            const start = (this.currentPage - 1) * this.entriesPerPage;
            const end = start + this.entriesPerPage;
            const pageData = this.filteredData.slice(start, end);

            this.tbody.innerHTML = '';
            pageData.forEach(row => {
                this.tbody.appendChild(row.element.cloneNode(true));
            });
        }

        this.updatePagination();
    }
}