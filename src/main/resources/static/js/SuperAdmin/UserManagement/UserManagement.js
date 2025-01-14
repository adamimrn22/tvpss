// document.addEventListener('DOMContentLoaded', function () {
//     const addUserTypeSelect = document.getElementById('userTypeSelect');
//     const daerahGroupAdd = document.getElementById('daerahGroup');
//     const schoolGroupAdd = document.getElementById('schoolGroup');
//     const districtSelect = daerahGroupAdd.querySelector('select');
//     const schoolSelect = schoolGroupAdd.querySelector('select');
//
//     const johorDistricts = [
//         "Skudai",
//         "Johor Bahru",
//         "Kulai",
//         "Pontian",
//         "Batu Pahat",
//         "Mersing",
//         "Kota Tinggi",
//         "Segamat",
//         "Tangkak",
//         "Muar",
//         "Kluang"
//     ];
//
//     function populateDistrictDropdown() {
//         districtSelect.innerHTML = '<option value="" selected disabled hidden>Select a District</option>';
//         johorDistricts.forEach(district => {
//             const option = document.createElement('option');
//             option.value = district;
//             option.textContent = district;
//             districtSelect.appendChild(option);
//         });
//     }
//
//     function handleDropdownVisibilityAndReadOnly(userTypeSelect, daerahGroup, schoolGroup) {
//         const selectedValue = userTypeSelect.value;
//         if (selectedValue === "ppdadmin") {
//             daerahGroup.style.display = "block";
//             schoolGroup.style.display = "none";
//         } else if (selectedValue === "schooladmin") {
//             daerahGroup.style.display = "block";
//             schoolGroup.style.display = "block";
//         } else {
//             daerahGroup.style.display = "none";
//             schoolGroup.style.display = "none";
//         }
//     }
//
//     function resetModalData(modalId) {
//         const modal = document.getElementById(modalId);
//         const inputs = modal.querySelectorAll('input');
//         // const selects = modal.querySelectorAll('select');
//         inputs.forEach(input => input.value = '');
//         // selects.forEach(select => select.value = '');
//         daerahGroupAdd.style.display = 'none';
//         schoolGroupAdd.style.display = 'none';
//     }
//
//     const baseUrl = /*[[@{/}]]*/ '';
//
//     function getSchoolsByDistrict(district) {
//         schoolSelect.innerHTML = '<option value="" selected disabled hidden>Loading...</option>';
//
//         fetch(baseUrl + 'schools/district/' + district, {  // Use the district parameter
//             method: 'GET',
//             headers: {
//                 'Accept': 'application/json',
//                 'Content-Type': 'application/json'
//             },
//             credentials: 'same-origin'
//         })
//             .then(response => response.json())
//             .then(data => {
//                 if (data && data.length > 0) {
//                     schoolSelect.innerHTML = '<option value="" selected disabled hidden>Select a School</option>';
//                     data.forEach(school => {
//                         const option = document.createElement('option');
//                         option.value = school.code;
//                         option.textContent = school.name;
//                         schoolSelect.appendChild(option);
//                     });
//                 } else {
//                     schoolSelect.innerHTML = '<option value="" selected disabled hidden>No schools available</option>';
//                 }
//             })
//             .catch(error => {
//                 console.error('Error fetching schools:', error);
//                 schoolSelect.innerHTML = '<option value="" selected disabled hidden>Error loading schools</option>';
//             });
//     }
//
//     addUserTypeSelect.addEventListener("change", function () {
//         handleDropdownVisibilityAndReadOnly(addUserTypeSelect, daerahGroupAdd, schoolGroupAdd);
//
//         schoolSelect.innerHTML = '<option value="" selected disabled hidden>Select a School</option>';
//
//         if (addUserTypeSelect.value === "schooladmin" && districtSelect.value) {
//             getSchoolsByDistrict(districtSelect.value);
//         }
//     });
//
//     districtSelect.addEventListener("click", function () {
//         const selectedDistrictOption = districtSelect.options[districtSelect.selectedIndex];
//
//         if (selectedDistrictOption) {
//             document.getElementById('addUserDistrict').value = selectedDistrictOption.value;
//         }
//     });
//
//     districtSelect.addEventListener("change", function () {
//         const selectedDistrict = districtSelect.value;
//
//         if (selectedDistrict) {
//             document.getElementById('addUserDistrict').value = selectedDistrict;
//             if (addUserTypeSelect.value === "schooladmin") {
//                 getSchoolsByDistrict(selectedDistrict);
//             }
//         } else {
//             document.getElementById('addUserDistrict').value = '';
//             schoolSelect.innerHTML = '<option value="" selected disabled hidden>Select a School</option>';
//         }
//     });
//
//     schoolSelect.addEventListener("change", function () {
//         const selectedSchoolCode = schoolSelect.value;
//
//         // Set the hidden schoolcode input value when a school is selected
//         document.getElementById('addUserSchoolCode').value = selectedSchoolCode;
//     });
//
//     populateDistrictDropdown();
//
//     handleDropdownVisibilityAndReadOnly(addUserTypeSelect, daerahGroupAdd, schoolGroupAdd);
//
//     const addUserModal = document.getElementById('addUserModal');
//     const editUserModal = document.getElementById('editUserModal');
//
//     const viewButtons = document.querySelectorAll('.hover-view');
//     const editButtons = document.querySelectorAll('.hover-edit');
//
//     viewButtons.forEach(button => {
//          button.addEventListener('click', async function () {
//             const userId = this.getAttribute('data-id');
//             console.log('test')
//             try {
//                 const response = await fetch(baseUrl + `Pengguna/ajax/${userId}`, {
//                     method: 'GET',
//                     headers: {
//                         'Accept': 'application/json',
//                         'Content-Type': 'application/json'
//                     },
//                     credentials: 'same-origin'
//                 });
//
//                 const data = await response.json();
//                 const user = data.user;;
//
//                 document.getElementById('viewFullName').value = user.name;
//                 document.getElementById('viewEmail').value = user.emailAddress;
//                 document.getElementById('viewUserTypeSelect').value = user.role.name;
//
//
//                 const viewDaerahGroup = document.getElementById('viewDaerahGroup');
//                 const viewSchoolGroup = document.getElementById('viewSchoolGroup');
//
//                 // Reset display first
//                 viewDaerahGroup.style.display = 'none';
//                 viewSchoolGroup.style.display = 'none';
//                 if (user.role.rolename.toLowerCase() === 'ppdadmin') {
//                     viewDaerahGroup.style.display = 'block';
//                     document.getElementById('viewDistrict').value= user.district;
//                 }
//
//                 if (user.role.rolename.toLowerCase() === 'schooladmin') {
//                     viewDaerahGroup.style.display = 'block';
//                     viewSchoolGroup.style.display = 'block';
//
//                     document.getElementById('viewDistrict').value= user.district;
//
//                     if (data.schoolCode) {
//                         document.getElementById('viewDistrict').value= user.district;
//                         document.getElementById('viewSchool').value= user.school.name;
//                     } else {
//                         console.log('No school code available for this user');
//                     }
//                 }
//             } catch (error) {
//                 console.error('Error in edit button click handler:', error);
//             }
//         });
//     });
//
//     editButtons.forEach(button => {
//         button.addEventListener('click', async function () {
//             const userId = this.getAttribute('data-id');
//             try {
//                 const response = await fetch(baseUrl + `Pengguna/ajax/${userId}`, {
//                     method: 'GET',
//                     headers: {
//                         'Accept': 'application/json',
//                         'Content-Type': 'application/json'
//                     },
//                     credentials: 'same-origin'
//                 });
//
//                 const data = await response.json();
//                 const user = data.user;
//                 document.getElementById('editFullName').value = user.name;
//                 document.getElementById('editEmail').value = user.emailAddress;
//                 // document.getElementById('editUserTypeSelect').value = user.role.name;
//                 document.getElementById('editUserId').value = user.id;
//
//             } catch (error) {
//                 console.error('Error in edit button click handler:', error);
//             }
//         });
//     });
//
//     addUserModal.addEventListener('hidden.bs.modal', function () {
//         resetModalData('addUserModal');
//     });
//
//     editUserModal.addEventListener('hidden.bs.modal', function () {
//         resetModalData('editUserModal');
//     });
//
//
// });
//
//
//

document.addEventListener('DOMContentLoaded', function () {
    const addUserTypeSelect = document.getElementById('userTypeSelect');
    const daerahGroupAdd = document.getElementById('daerahGroup');
    const schoolGroupAdd = document.getElementById('schoolGroup');
    const districtSelect = daerahGroupAdd.querySelector('select');
    const schoolSelect = schoolGroupAdd.querySelector('select');

    const johorDistricts = [
        "Skudai", "Johor Bahru", "Kulai", "Pontian", "Batu Pahat", "Mersing",
        "Kota Tinggi", "Segamat", "Tangkak", "Muar", "Kluang"
    ];

    function populateDistrictDropdown() {
        districtSelect.innerHTML = '<option value="" selected disabled hidden>Select a District</option>';
        johorDistricts.forEach(district => {
            const option = document.createElement('option');
            option.value = district;
            option.textContent = district;
            districtSelect.appendChild(option);
        });
    }

    function handleDropdownVisibilityAndReadOnly(userTypeSelect, daerahGroup, schoolGroup) {
        const selectedValue = userTypeSelect.value;
        if (selectedValue === "ppdadmin") {
            daerahGroup.style.display = "block";
            schoolGroup.style.display = "none";
        } else if (selectedValue === "schooladmin") {
            daerahGroup.style.display = "block";
            schoolGroup.style.display = "block";
        } else {
            daerahGroup.style.display = "none";
            schoolGroup.style.display = "none";
        }
    }

    function resetModalData(modalId) {
        const modal = document.getElementById(modalId);
        const inputs = modal.querySelectorAll('input');

        inputs.forEach(input => {
             if (input.name !== "_csrf") {
                input.value = '';
            }
        });

        const selects = modal.querySelectorAll('select');
        selects.forEach(select => select.selectedIndex = 0);

        daerahGroupAdd.style.display = 'none';
        schoolGroupAdd.style.display = 'none';
    }


    const baseUrl = /*[[@{/}]]*/ '';

    function getSchoolsByDistrict(district) {
        schoolSelect.innerHTML = '<option value="" selected disabled hidden>Loading...</option>';
        fetch(baseUrl + 'schools/district/' + district, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            credentials: 'same-origin'
        })
            .then(response => response.json())
            .then(data => {
                if (data && data.length > 0) {
                    schoolSelect.innerHTML = '<option value="" selected disabled hidden>Select a School</option>';
                    data.forEach(school => {
                        const option = document.createElement('option');
                        option.value = school.code;
                        option.textContent = school.name;
                        schoolSelect.appendChild(option);
                    });
                } else {
                    schoolSelect.innerHTML = '<option value="" selected disabled hidden>No schools available</option>';
                }
            })
            .catch(error => {
                console.error('Error fetching schools:', error);
                schoolSelect.innerHTML = '<option value="" selected disabled hidden>Error loading schools</option>';
            });
    }

    addUserTypeSelect.addEventListener("change", function () {
        handleDropdownVisibilityAndReadOnly(addUserTypeSelect, daerahGroupAdd, schoolGroupAdd);
        schoolSelect.innerHTML = '<option value="" selected disabled hidden>Select a School</option>';
        if (addUserTypeSelect.value === "schooladmin" && districtSelect.value) {
            getSchoolsByDistrict(districtSelect.value);
        }
    });

    districtSelect.addEventListener("click", function () {
        const selectedDistrictOption = districtSelect.options[districtSelect.selectedIndex];
        if (selectedDistrictOption) {
            document.getElementById('addUserDistrict').value = selectedDistrictOption.value;
        }
    });

    districtSelect.addEventListener("change", function () {
        const selectedDistrict = districtSelect.value;
        if (selectedDistrict) {
            document.getElementById('addUserDistrict').value = selectedDistrict;
            if (addUserTypeSelect.value === "schooladmin") {
                getSchoolsByDistrict(selectedDistrict);
            }
        } else {
            document.getElementById('addUserDistrict').value = '';
            schoolSelect.innerHTML = '<option value="" selected disabled hidden>Select a School</option>';
        }
    });

    schoolSelect.addEventListener("change", function () {
        const selectedSchoolCode = schoolSelect.value;
        document.getElementById('addUserSchoolCode').value = selectedSchoolCode;
    });

    populateDistrictDropdown();
    handleDropdownVisibilityAndReadOnly(addUserTypeSelect, daerahGroupAdd, schoolGroupAdd);

    const addUserModal = document.getElementById('addUserModal');
    const editUserModal = document.getElementById('editUserModal');

    // Delegated event listeners for Edit, View, and Delete buttons
    document.querySelector('table').addEventListener('click', function (e) {
        const button = e.target.closest('button');
        if (!button) return; // If the clicked element is not a button, return

        const userId = button.getAttribute('data-id');
        if (!userId) return; // If there is no data-id, return

        if (button.classList.contains('hover-view')) {
            viewUser(userId);
        } else if (button.classList.contains('hover-edit')) {
            editUser(userId);
        } else if (button.classList.contains('hover-delete')) {
            deleteUser(userId);
        }
    });

    async function viewUser(userId) {
        // Handle the view button logic
        console.log('View user with ID:', userId);

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

            // Reset display first
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

                if (data.schoolCode) {
                    document.getElementById('viewDistrict').value = user.district;
                    document.getElementById('viewSchool').value = user.school.name;
                } else {
                    console.log('No school code available for this user');
                }
            }
        } catch (error) {
            console.error('Error in edit button click handler:', error);
        }
    }

    async function editUser(userId) {
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
            // document.getElementById('editUserTypeSelect').value = user.role.name;
            document.getElementById('editUserId').value = user.id;

        } catch (error) {
            console.error('Error in edit button click handler:', error);
        }
    }

    function deleteUser(userId) {
        document.getElementById("userIdToDelete").value = userId
    }

    addUserModal.addEventListener('hidden.bs.modal', function () {
        resetModalData('addUserModal');
    });

    editUserModal.addEventListener('hidden.bs.modal', function () {
        resetModalData('editUserModal');
    });
});
