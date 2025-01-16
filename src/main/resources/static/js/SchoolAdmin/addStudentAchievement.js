// Global variables
let currentModalPage = 1;
let currentMainPage = 1;
const itemsPerPage = 10;
let students = [];
let selectedStudents = [];

// Initialize when document is ready
document.addEventListener('DOMContentLoaded', () => {
	initializeDateInput();
	initializeEventListeners();
	fetchStudents();
});

function initializeDateInput() {
	const today = new Date().toISOString().split('T')[0];
	document.getElementById('modalDate').value = today;
}

function initializeEventListeners() {
	document.getElementById('searchInput').addEventListener('input', debounce(handleSearch, 300));
	document.getElementById('modalDate').addEventListener('change', handleDateChange);

	// Add event listener for the form submit
	document.getElementById('studentForm').addEventListener('submit', function(event) {
		// Prevent the default form submission
		event.preventDefault();

		// Initialize the achievements array
		let achievements = [];

		// Collect all the rows from the selected students table
		const rows = document.querySelectorAll('#selectedStudentsTable tbody tr');

		// Loop through each row and collect data for each student
		rows.forEach((row, index) => {
			const studentAchievement = {
				studentIdentificationNumber: row.querySelector(`input[name="achievements[${index}].studentIdentificationNumber"]`).value,
				studentName: row.querySelector(`input[name="achievements[${index}].studentName"]`).value,
				studentDateAchievement: row.querySelector(`input[name="achievements[${index}].studentDateAchievement"]`).value,
				studentTypeAchievement: row.querySelector(`input[name="achievements[${index}].studentTypeAchievement"]`).value,
				achievementInformation: row.querySelector(`input[name="achievements[${index}].achievementInformation"]`).value
			};

			// Add the student achievement object to the achievements array
			achievements.push(studentAchievement);
		});

		// Log the structured data for debugging
		console.log("Reformatted Achievements Data:", achievements);

		// Create a hidden input to hold the formatted data as a JSON string
		const hiddenInput = document.createElement('input');
		hiddenInput.type = 'hidden';
		hiddenInput.name = 'achievementsStudent'; // The name of the parameter expected on the backend
		hiddenInput.value = JSON.stringify(achievements); // Store the array as a JSON string

		// Append the hidden input to the form
		this.appendChild(hiddenInput);

		// Now submit the form traditionally
		this.submit();
	});



}

function debounce(func, wait) {
	let timeout;
	return function executedFunction(...args) {
		const later = () => {
			clearTimeout(timeout);
			func(...args);
		};
		clearTimeout(timeout);
		timeout = setTimeout(later, wait);
	};
}

function fetchStudents() {
	fetch(`${contextPath}StudentAchievement/addStudentAchievement/by-school/${schoolCode}`)
		.then(response => response.json())
		.then(data => {
			if (data.success) {
				students = data.data;
				console.log(students);
				updateModalTable(students);
			} else {
				alert(data.message);
			}
		})
		.catch(error => {
			console.error('Error fetching students:', error);
			alert('Failed to fetch students');
		});
}

function handleSearch(event) {
	const searchTerm = event.target.value.toLowerCase();
	const filteredStudents = students.filter(student =>
		student.identificationNumber.toLowerCase().includes(searchTerm) ||
		student.name.toLowerCase().includes(searchTerm)
	);
	currentModalPage = 1; // Reset to first page when searching
	updateModalTable(filteredStudents);
}

function updateModalTable(studentsToShow) {
	const tbody = document.querySelector('#searchResultsTable tbody');
	tbody.innerHTML = '';

	const startIndex = (currentModalPage - 1) * itemsPerPage;
	const endIndex = Math.min(startIndex + itemsPerPage, studentsToShow.length);
	const paginatedStudents = studentsToShow.slice(startIndex, endIndex);

	if (paginatedStudents.length === 0) {
		tbody.innerHTML = `
            <tr>
                <td colspan="3" class="text-center">No students found</td>
            </tr>
        `;
	} else {
		paginatedStudents.forEach(student => {
			const isSelected = selectedStudents.some(s => s.identificationNumber === student.identificationNumber);
			const row = document.createElement('tr');
			row.innerHTML = `
                <td>${student.identificationNumber}</td>
                <td>${student.name}</td>
                <td>
                    <button type="button" 
                            class="btn btn-sm ${isSelected ? 'btn-danger' : 'btn-primary'}"
                            onclick="handleStudentAction('${student.identificationNumber}', '${student.name}')">
                        ${isSelected ? 'Remove' : 'Add'}
                    </button>
                </td>
            `;
			tbody.appendChild(row);
		});
	}

	// Update pagination info
	updatePaginationInfo('modal', startIndex + 1, endIndex, studentsToShow.length);
	updatePagination('modal', studentsToShow, startIndex, endIndex);
}

function updateMainTable() {
	const tbody = document.querySelector('#selectedStudentsTable tbody');
	tbody.innerHTML = '';  // Clear any existing rows

	const startIndex = (currentMainPage - 1) * itemsPerPage;
	const endIndex = Math.min(startIndex + itemsPerPage, selectedStudents.length);
	const paginatedStudents = selectedStudents.slice(startIndex, endIndex);

	if (paginatedStudents.length === 0) {
		tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center">No students selected</td>
            </tr>
        `;
	} else {
		paginatedStudents.forEach((student, index) => {
			const row = document.createElement('tr');

			// Dynamically generate row content for each student
			row.innerHTML = `
                <td>
                    <input type="hidden" name="achievements[${startIndex + index}].studentIdentificationNumber" value="${student.identificationNumber}">
                    ${student.identificationNumber}
                </td>
                <td>
                    ${student.name}
                    <input type="hidden" name="achievements[${startIndex + index}].studentName" value="${student.name}">
                </td>
                <td>
                    <input type="date" class="form-control" 
                           name="achievements[${startIndex + index}].studentDateAchievement" 
                           value="${student.date}" required
                           onchange="updateStudentDate('${student.identificationNumber}', this.value)">
                </td>
                <td>
                    <input type="text" class="form-control" 
                           name="achievements[${startIndex + index}].studentTypeAchievement" 
                           value="${student.jenisPencapaian}" required
                           onchange="updateStudentField('${student.identificationNumber}', 'jenisPencapaian', this.value)">
                </td>
                <td>
                    <input type="text" class="form-control" 
                           name="achievements[${startIndex + index}].achievementInformation" 
                           value="${student.maklumatPencapaian}" required
                           onchange="updateStudentField('${student.identificationNumber}', 'maklumatPencapaian', this.value)">
                </td>
                <td>
                    <button type="button" class="btn btn-danger btn-sm" 
                            onclick="removeStudent('${student.identificationNumber}')">
                        Remove
                    </button>
                </td>
            `;
			tbody.appendChild(row);
		});
	}

	updatePaginationInfo('main', startIndex + 1, endIndex, selectedStudents.length);
	updatePagination('main', selectedStudents.length);
}



function handleStudentAction(studentIdentificationNumber, studentName) {
	const selectedIndex = selectedStudents.findIndex(s => s.identificationNumber === studentIdentificationNumber);
	const date = document.getElementById('modalDate').value;

	if (selectedIndex === -1) {
		// Add student
		selectedStudents.push({
			identificationNumber: studentIdentificationNumber,
			name: studentName,
			date: date,
			jenisPencapaian: '',
			maklumatPencapaian: ''
		});
	} else {
		// Remove student
		selectedStudents.splice(selectedIndex, 1);
	}

	updateMainTable();
	updateModalTable(students); // This will refresh the modal table with updated buttons
}

function removeStudent(studentId) {
	const index = selectedStudents.findIndex(s => s.identificationNumber === studentId);
	if (index !== -1) {
		selectedStudents.splice(index, 1);
		updateMainTable();
		updateModalTable(students);
	}
}

function updateStudentDate(studentId, newDate) {
	// Find the student in the selectedStudents array
	const student = selectedStudents.find(student => student.identificationNumber === studentId);

	// Update the student date in the array
	if (student) {
		student.date = newDate;
	}

	// Manually update the input field value if needed (but it should already be done automatically)
	const inputField = document.querySelector(`input[name="achievements[${selectedStudents.indexOf(student)}].studentDateAchievement"]`);
	if (inputField) {
		inputField.value = newDate;
	}
}

function updateStudentField(studentId, field, newValue) {
	// Find the student object in the JavaScript model
	const student = selectedStudents.find(student => student.identificationNumber === studentId);

	if (student) {
		// Update the student data in the JavaScript model
		student[field] = newValue;

		// Find the correct input element dynamically based on the field name
		const inputElement = document.querySelector(`input[name="achievements[${selectedStudents.indexOf(student)}].${field}"]`);

		if (inputElement) {
			// Update the input field's value to reflect the change
			inputElement.value = newValue;
		}
	}
}


function handleDateChange(event) {
	const newDate = event.target.value;
	// Only update date for newly added students
	students.forEach(student => {
		if (!selectedStudents.some(s => s.identificationNumber === student.identificationNumber)) {
			student.date = newDate;
		}
	});
}

// Update the data before the form submission
function updateDataBeforeSubmit() {
	// Loop through all the rows in the table and update the model
	const rows = document.querySelectorAll('#selectedStudentsTable tbody tr');

	rows.forEach((row, index) => {
		const studentId = row.querySelector('input[name^="achievements"]')?.value; // Get the student ID

		// Update the model based on the input values in the row
		const student = selectedStudents.find(student => student.identificationNumber === studentId);

		if (student) {
			student.studentDateAchievement = row.querySelector(`input[name="achievements[${index}].studentDateAchievement"]`).value;
			student.studentTypeAchievement = row.querySelector(`input[name="achievements[${index}].studentTypeAchievement"]`).value;
			student.achievementInformation = row.querySelector(`input[name="achievements[${index}].achievementInformation"]`).value;
		}
	});

	// Optionally, log the updated model for debugging purposes
	console.log("Updated student data:", selectedStudents);
}

// Pagination functions (same as your original code)
function updatePaginationInfo(type, start, end, total) {
	document.getElementById(`${type}StartIndex`).textContent = total === 0 ? 0 : start;
	document.getElementById(`${type}EndIndex`).textContent = end;
	document.getElementById(`${type}TotalItems`).textContent = total;
}

function updatePagination(type, studentsToShow, startIndex, endIndex) {
	const totalItems = studentsToShow.length;
	const totalPages = Math.ceil(totalItems / itemsPerPage);
	const currentPage = type === 'modal' ? currentModalPage : currentMainPage;
	const ul = document.getElementById(`${type}Pagination`);
	ul.innerHTML = '';

	if (totalPages <= 1) return;

	// Previous button
	ul.appendChild(createPaginationItem('Previous', currentPage > 1, () => {
		if (type === 'modal') {
			currentModalPage--;
			updateModalTable(studentsToShow);
		} else {
			currentMainPage--;
			updateMainTable();
		}
	}));

	// Page numbers
	for (let i = 1; i <= totalPages; i++) {
		const isActive = i === currentPage;
		ul.appendChild(createPaginationItem(i, true, () => {
			if (type === 'modal') {
				currentModalPage = i;
				updateModalTable(studentsToShow);
			} else {
				currentMainPage = i;
				updateMainTable();
			}
		}, isActive));
	}

	// Next button
	ul.appendChild(createPaginationItem('Next', currentPage < totalPages, () => {
		if (type === 'modal') {
			currentModalPage++;
			updateModalTable(studentsToShow);
		} else {
			currentMainPage++;
			updateMainTable();
		}
	}));
}

function createPaginationItem(text, enabled, onClick, isActive = false) {
	const li = document.createElement('li');
	li.className = `page-item ${!enabled ? 'disabled' : ''} ${isActive ? 'active' : ''}`;

	const a = document.createElement('a');
	a.className = 'page-link';
	a.href = '#';
	a.textContent = text;

	if (enabled) {
		a.addEventListener('click', (e) => {
			e.preventDefault();
			onClick();
		});
	}

	li.appendChild(a);
	return li;
}

// Make necessary functions available globally
window.handleStudentAction = handleStudentAction;
window.removeStudent = removeStudent;
window.updateStudentDate = updateStudentDate;
window.updateStudentField = updateStudentField;
