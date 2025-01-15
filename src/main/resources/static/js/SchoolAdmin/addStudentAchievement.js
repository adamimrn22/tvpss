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
		student.id.toLowerCase().includes(searchTerm) ||
		student.name.toLowerCase().includes(searchTerm)
	);
	currentModalPage = 1; // Reset to first page when searching
	updateModalTable(filteredStudents, searchTerm); // Pass searchTerm to updateModalTable
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
			const isSelected = selectedStudents.some(s => s.id === student.id);
			const row = document.createElement('tr');
			row.innerHTML = `
                <td>${student.id}</td>
                <td>${student.name}</td>
                <td>
                    <button type="button" 
                            class="btn btn-sm ${isSelected ? 'btn-danger' : 'btn-primary'}"
                            onclick="handleStudentAction('${student.id}', '${student.name}')">
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
	tbody.innerHTML = '';

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
			const absoluteIndex = startIndex + index;
			row.innerHTML = `
                <td>
                    <input type="hidden" name="achievements[${absoluteIndex}].studentId" value="${student.id}">
                    ${student.id}
                </td>
                <td>${student.name}</td>
                <td>
                    <input type="date" class="form-control" 
                           name="achievements[${absoluteIndex}].date" 
                           value="${student.date}" required
                           onchange="updateStudentDate('${student.id}', this.value)">
                </td>
                <td>
                    <input type="text" class="form-control" 
                           name="achievements[${absoluteIndex}].jenisPencapaian" 
                           value="${student.jenisPencapaian}" required
                           onchange="updateStudentField('${student.id}', 'jenisPencapaian', this.value)">
                </td>
                <td>
                    <input type="text" class="form-control" 
                           name="achievements[${absoluteIndex}].maklumatPencapaian" 
                           value="${student.maklumatPencapaian}" required
                           onchange="updateStudentField('${student.id}', 'maklumatPencapaian', this.value)">
                </td>
                <td>
                    <button type="button" class="btn btn-danger btn-sm" 
                            onclick="removeStudent('${student.id}')">
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

function handleStudentAction(studentId, studentName) {
	const selectedIndex = selectedStudents.findIndex(s => s.id === studentId);
	const date = document.getElementById('modalDate').value;

	if (selectedIndex === -1 && studentName) {
		// Add student
		selectedStudents.push({
			id: studentId,
			name: studentName,
			date: date,
			jenisPencapaian: '',
			maklumatPencapaian: ''
		});
	} else if (selectedIndex !== -1) {
		// Remove student
		selectedStudents.splice(selectedIndex, 1);
	}

	updateMainTable();
	updateModalTable(students); // This will refresh the modal table with updated buttons
}


function removeStudent(studentId) {
	const index = selectedStudents.findIndex(s => s.id === studentId);
	if (index !== -1) {
		selectedStudents.splice(index, 1);
		updateMainTable();
		updateModalTable(students);
	}
}

function updateStudentDate(studentId, newDate) {
	const student = selectedStudents.find(s => s.id === studentId);
	if (student) {
		student.date = newDate;
	}
}

function updateStudentField(studentId, field, value) {
	const student = selectedStudents.find(s => s.id === studentId);
	if (student) {
		student[field] = value;
	}
}

function handleDateChange(event) {
	const newDate = event.target.value;
	// Only update date for newly added students
	students.forEach(student => {
		if (!selectedStudents.some(s => s.id === student.id)) {
			student.date = newDate;
		}
	});
}

// function handleFormSubmit(event) {
// 	event.preventDefault();
//
// 	if (selectedStudents.length === 0) {
// 		alert('Please select at least one student');
// 		return;
// 	}
//
// 	// Validate all required fields
// 	const form = event.target;
// 	if (!form.checkValidity()) {
// 		form.reportValidity();
// 		return;
// 	}
//
// 	const formData = new FormData(form);
//
// 	fetch(form.action, {
// 		method: 'POST',
// 		body: formData
// 	})
// 		.then(response => response.json())
// 		.then(data => {
// 			if (data.success) {
// 				alert('Achievements saved successfully');
// 				window.location.href = `${contextPath}student/achievement/list`;
// 			} else {
// 				alert(data.message);
// 			}
// 		})
// 		.catch(error => {
// 			console.error('Error submitting form:', error);
// 			alert('Failed to save achievements');
// 		});
// }

// Pagination functions
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

	if (totalPages <= 1) return; // Don't show pagination if only one page

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