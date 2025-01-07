document.addEventListener('DOMContentLoaded', function () {
    const addUserTypeSelect = document.getElementById('userTypeSelect');
    const daerahGroupAdd = document.getElementById('daerahGroup');
    const schoolGroupAdd = document.getElementById('schoolGroup');
    const districtSelect = daerahGroupAdd.querySelector('select');
    const schoolSelect = schoolGroupAdd.querySelector('select');

    const johorDistricts = [
        "Skudai",
        "Johor Bahru",
        "Kulai",
        "Pontian",
        "Batu Pahat",
        "Mersing",
        "Kota Tinggi",
        "Segamat",
        "Tangkak",
        "Muar",
        "Kluang"
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
        const selects = modal.querySelectorAll('select');
        inputs.forEach(input => input.value = '');
        selects.forEach(select => select.value = '');
        daerahGroupAdd.style.display = 'none';
        schoolGroupAdd.style.display = 'none';
    }

    const baseUrl = /*[[@{/}]]*/ '';
    console.log('Base URL:', baseUrl);

    function getSchoolsByDistrict(district) {
        schoolSelect.innerHTML = '<option value="" selected disabled hidden>Loading...</option>';
        console.log('Base URL:', baseUrl + `'schools/district/' + district`);

        fetch(baseUrl + 'schools/district/' + district, {  // Use the district parameter
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

        // Set the hidden schoolcode input value when a school is selected
        document.getElementById('addUserSchoolCode').value = selectedSchoolCode;
    });

    populateDistrictDropdown();

    handleDropdownVisibilityAndReadOnly(addUserTypeSelect, daerahGroupAdd, schoolGroupAdd);

    const addUserModal = document.getElementById('addUserModal');
    const editUserModal = document.getElementById('editUserModal');

    const editButtons = document.querySelectorAll('.hover-edit');
    const editRole = document.getElementById('editUserTypeSelect').value;

    // Conditionally show/hide district and school fields based on the role
    const editDaerahGroup = document.getElementById('editDaerahGroup');
    const editSchoolGroup = document.getElementById('editSchoolGroup');

    if (editRole === 'schooladmin') {
        editDaerahGroup.style.display = 'block';
        editSchoolGroup.style.display = 'block';  // Show both district and school fields
    } else if (editRole === 'ppdadmin') {
        editDaerahGroup.style.display = 'block';  // Show only district field
        editSchoolGroup.style.display = 'none';
    } else {
        editDaerahGroup.style.display = 'none';  // Hide both district and school fields
        editSchoolGroup.style.display = 'none';
    }

    editButtons.forEach(button => {
        button.addEventListener('click', async function () {
            const userId = this.getAttribute('data-id');
            console.log('User ID:', userId);

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
                console.log('Full response data:', data);
                console.log('User role:', user.role);
                console.log('School code:', data.schoolCode);

                // Populate basic user data
                document.getElementById('editFullName').value = user.name;
                document.getElementById('editEmail').value = user.emailAddress;
                document.getElementById('editUserTypeSelect').value = user.role.name;
                document.getElementById('userId').value = user.id;

                // Handle role-specific fields
                const daerahGroup = document.getElementById('editDaerahGroup');
                const schoolGroup = document.getElementById('editSchoolGroup');

                // Reset display first
                daerahGroup.style.display = 'none';
                schoolGroup.style.display = 'none';
                if (user.role.rolename.toLowerCase() === 'ppdadmin') {
                    daerahGroup.style.display = 'block';
                    document.getElementById('editDistrict').value= user.district;
                }
                console.log('User role name:', user.role.rolename);

                if (user.role.rolename.toLowerCase() === 'schooladmin') {
                    console.log('User is school admin');
                    daerahGroup.style.display = 'block';
                    schoolGroup.style.display = 'block';

                    document.getElementById('editDistrict').value= user.district;

                    if (data.schoolCode) {
                        document.getElementById('editDistrict').value= user.district;
                        document.getElementById('editSchool').value= user.school.name;
                    } else {
                        console.log('No school code available for this user');
                    }
                } else if (user.role.rolename.toLowerCase() === 'ppd-admin') {
                    console.log('User is PPD admin');
                    daerahGroup.style.display = 'block';
                    document.getElementById('editDistrict').value = user.district || '';
                } else {
                    console.log('User has different role:', user.role.rolename);
                }
            } catch (error) {
                console.error('Error in edit button click handler:', error);
            }
        });
    });

    addUserModal.addEventListener('hidden.bs.modal', function () {
        resetModalData('addUserModal');
    });

    editUserModal.addEventListener('hidden.bs.modal', function () {
        resetModalData('editUserModal');
    });

});

document.querySelectorAll('.hover-delete').forEach(button => {
    button.addEventListener('click', function () {
        // Get the user ID from the data-id attribute
        var userId = this.getAttribute('data-id');

        // Set the userId value in the hidden input inside the modal
        document.getElementById('userIdToDelete').value = userId;
    });
});
