class DatePicker {
    constructor(dateInputId, datePickerId, monthYearDisplayId, datePickerBodyId, prevMonthId, nextMonthId, calendarViewId, yearViewId) {
        this.dateInput = document.getElementById(dateInputId);
        this.datePicker = document.getElementById(datePickerId);
        this.monthYearDisplay = document.getElementById(monthYearDisplayId);
        this.datePickerBody = document.getElementById(datePickerBodyId);
        this.prevMonth = document.getElementById(prevMonthId);
        this.nextMonth = document.getElementById(nextMonthId);
        this.calendarView = document.getElementById(calendarViewId);
        this.yearView = document.getElementById(yearViewId);

        this.currentDate = new Date();
        this.yearRangeStart = null;

        this.init();
    }

    init() {
        this.renderCalendar();
        this.updateNavigation();
        this.setupEventListeners();
    }

    renderCalendar() {
        const year = this.currentDate.getFullYear();
        const month = this.currentDate.getMonth();
        this.monthYearDisplay.textContent = `${this.currentDate.toLocaleString('default', { month: 'long' })} ${year}`;

        const firstDayOfMonth = new Date(year, month, 1).getDay();
        const daysInMonth = new Date(year, month + 1, 0).getDate();

        let daysHTML = '';
        let dayCounter = 1;

        // Add empty cells before the first day
        for (let i = 0; i < firstDayOfMonth; i++) {
            daysHTML += '<td class="disabled"></td>';
        }

        // Add days of the month
        for (let i = firstDayOfMonth; i < 7 * 6; i++) {
            if (i >= firstDayOfMonth && dayCounter <= daysInMonth) {
                daysHTML += `<td data-date="${year}-${String(month + 1).padStart(2, '0')}-${String(dayCounter).padStart(2, '0')}">${dayCounter}</td>`;
                dayCounter++;
            } else {
                daysHTML += '<td class="disabled"></td>';
            }
            if ((i + 1) % 7 === 0) {
                daysHTML += '</tr><tr>';
            }
        }

        this.datePickerBody.innerHTML = `<tr>${daysHTML}</tr>`;
    }

    renderYearPicker() {
        const rangeSize = 12; // Number of years to show
        this.yearRangeStart = this.yearRangeStart || this.currentDate.getFullYear() - 6; // Default range
        let yearsHTML = '';

        for (let year = this.yearRangeStart; year < this.yearRangeStart + rangeSize; year++) {
            yearsHTML += `<div data-year="${year}" class="${year === this.currentDate.getFullYear() ? 'selected' : ''}">${year}</div>`;
        }

        this.yearView.innerHTML = yearsHTML;
    }

    updateNavigation() {
        this.prevMonth.addEventListener('click', () => {
            if (!this.calendarView.classList.contains('hidden')) {
                this.currentDate.setMonth(this.currentDate.getMonth() - 1);
                this.renderCalendar();
            } else {
                this.yearRangeStart -= 12; // Move back by 12 years when in the year view
                this.renderYearPicker();
            }
        });

        this.nextMonth.addEventListener('click', () => {
            if (!this.calendarView.classList.contains('hidden')) {
                this.currentDate.setMonth(this.currentDate.getMonth() + 1);
                this.renderCalendar();
            } else {
                this.yearRangeStart += 12; // Move forward by 12 years when in the year view
                this.renderYearPicker();
            }
        });
    }

    setupEventListeners() {
        // Switch to year view when clicking month/year display
        this.monthYearDisplay.addEventListener('click', () => {
            if (!this.calendarView.classList.contains('hidden')) {
                this.calendarView.classList.add('hidden');
                this.yearView.classList.remove('hidden');
                this.renderYearPicker();
            } else {
                this.calendarView.classList.remove('hidden');
                this.yearView.classList.add('hidden');
                this.renderCalendar();
            }
        });

        // Select a year and switch back to the calendar view
        this.yearView.addEventListener('click', (e) => {
            if (e.target.dataset.year) {
                this.currentDate.setFullYear(e.target.dataset.year);
                this.yearView.classList.add('hidden');
                this.calendarView.classList.remove('hidden');
                this.renderCalendar();
            }
        });

        // Open the date picker when clicking the input field
        this.dateInput.addEventListener('click', () => {
            this.datePicker.style.display = this.datePicker.style.display === 'block' ? 'none' : 'block';
            this.renderCalendar();
            this.updateNavigation();
        });

        // Close the date picker when clicking outside
        document.addEventListener('click', (e) => {
            if (!e.target.closest('.date-picker-container')) {
                this.datePicker.style.display = 'none';
            }
        });

        // Select a date from the date picker
        this.datePickerBody.addEventListener('click', (e) => {
            if (e.target.tagName === 'TD' && e.target.dataset.date) {
                this.dateInput.value = e.target.dataset.date;
                this.datePicker.style.display = 'none';
            }
        });
    }
}