

// Wait for the DOM to be fully loaded before running the script
document.addEventListener('DOMContentLoaded', function () {

    const barCtx = document.getElementById('barChart').getContext('2d');
    new Chart(barCtx, {
        type: 'bar',
        data: {
            labels: ['Super Admin', 'State Admin', 'Ppd Admin', 'School Admin'], // The roles
            datasets: [{
                label: 'Bilangan Pengguna',
                data: [loginByRole.SuperAdmin, loginByRole.StateAdmin, loginByRole.PpdAdmin, loginByRole.SchoolAdmin], // Using the counts from loginCountsByRole
                backgroundColor: ['#25565B', '#2ECAFF', '#228FA6', '#FFD700']
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            indexAxis: 'y', // Horizontal bar chart
            scales: {
                x: {
                    type: 'linear',  // Treat the x-axis as a linear scale (for numbers)
                    position: 'top', // Position the x-axis at the top (optional)
                    ticks: {
                        stepSize: 1,  // Ensures the x-axis increments in whole numbers (no decimals)
                        beginAtZero: true // Starts from zero
                    },
                    title: {
                        display: true,
                        text: 'Number of Users'
                    }
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Roles'
                    }
                }
            },
            plugins: {
                legend: { display: false } // Hide the legend if not needed
            }
        }
    });



    const lineCtx = document.getElementById('lineChart').getContext('2d');

    console.log(loginCountsByRole)
    // Prepare the datasets dynamically based on the role login data
    const datasets = Object.entries(loginCountsByRole).map(([role, counts]) => {
        return {
            label: role, // Use the role name as the label
            data: counts, // The counts for each hour
            borderColor: getBorderColorForRole(role), // Set a color based on the role
            borderWidth: 2,
            fill: false
        };
    });

    new Chart(lineCtx, {
        type: 'line',
        data: {
            labels: labels, // Hours
            datasets: datasets // The dynamic datasets
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Hour'
                    }
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Bilangan Pengguna Log Masuk'
                    }
                }
            },
            plugins: {
                legend: {
                    display: true,
                    position: 'top'
                },
                tooltip: {
                    mode: 'index',
                    intersect: false
                }
            }
        }
    });

    function getBorderColorForRole(role) {
        switch (role) {
            case 'superadmin':
                return '#37285C';
            case 'stateadmin':
                return '#94FFCC';
            case 'ppdadmin':
                return '#0096BF';
            case 'schooladmin':
                return '#FF5733';
            default:
                return '#000000'; // Default color
        }
    }


});