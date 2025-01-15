// Wait until the DOM is fully loaded before running the code
document.addEventListener('DOMContentLoaded', function() {
    // Event listener for 'Edit' button click
    document.addEventListener('click', function (event) {
        if (event.target && event.target.closest('.hover-delete')) {
            document.getElementById("crewRoleToDelete").value = event.target.closest('.hover-delete').getAttribute('data-id')
        }
    });

    document.addEventListener('click', function(event) {
        // Check if the clicked element is the button with class 'hover-edit'
        if (event.target && event.target.closest('.hover-edit')) {
            // Retrieve the equipment ID from the button's data-id attribute
            let crewRoleId = event.target.closest('.hover-edit').getAttribute('data-id');

            console.log(crewRoleId)
            const baseUrl = /*[[@{/}]]*/ '';

            // Send a GET request to fetch the equipment details
            fetch(baseUrl + 'CrewRole/ajax/' + crewRoleId)
                .then(response => response.json())
                .then(data => {
                    // The response is a map with the equipment ID as the key
                    console.log(data)
                    if (data){
                        document.getElementById('crewRoleID').value = data.id;
                        document.getElementById('crewRoleName').value = data.name;
                    }
                })
                .catch(() => {
                    alert('Failed to load crew role data.');
                });
        }
    });
});
