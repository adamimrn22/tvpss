// Wait until the DOM is fully loaded before running the code
document.addEventListener('DOMContentLoaded', function() {
    // Event listener for 'Edit' button click
    document.addEventListener('click', function (event) {
        if (event.target && event.target.closest('.hover-delete')) {
            document.getElementById("equipmentIDToDelete").value = event.target.closest('.hover-delete').getAttribute('data-id')
        }
    });

    document.addEventListener('click', function(event) {
        // Check if the clicked element is the button with class 'hover-edit'
        if (event.target && event.target.closest('.hover-edit')) {
            // Retrieve the equipment ID from the button's data-id attribute
            var equipmentId = event.target.closest('.hover-edit').getAttribute('data-id');

            const baseUrl = /*[[@{/}]]*/ '';

            // Send a GET request to fetch the equipment details
            fetch(baseUrl + 'EquipmentManagement/ajax/' + equipmentId)
                .then(response => response.json())
                .then(data => {
                    // The response is a map with the equipment ID as the key
                    var equipment = data[equipmentId];
                    if (equipment){
                        document.getElementById('equipmentID').value = equipment.id;
                        document.getElementById('editItemName').value = equipment.equipmentName;
                        document.getElementById('editItemType').value = equipment.equipmentType;
                        document.getElementById('editItemLocation').value = equipment.location;
                        document.getElementById('dateAdded').value = equipment.dateAdded;  // Ensure the date format is 'yyyy-MM-dd'
                        document.getElementById('editItemStatus').value = equipment.status;

                        // Store the equipment ID in the modal for later use (in case you need to update it)
                        document.getElementById('editEquipmentModal').setAttribute('data-equipment-id', equipment.id);
                    }
                })
                .catch(() => {
                    alert('Failed to load equipment data.');
                });
        }
    });
});
