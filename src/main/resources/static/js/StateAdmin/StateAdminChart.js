document.addEventListener("DOMContentLoaded", function() {

    const barCtx = document.getElementById('barChart').getContext('2d');
    new Chart(barCtx, {
        type: 'bar',
        data: {
            labels: ['Versi 1', 'Versi 2', 'Versi 3', 'Versi 4'],  // X-axis labels
            datasets: [{
                label: 'Bilangan Sekolah',  // The label for the bar chart
                data: [versionZero, versionOne, versionTwo, versionThree, versionFour],  // Dynamic data from Spring model
                backgroundColor: ['#25565B', '#2ECAFF', '#228FA6', '#2EA7A6', '#2BD6AFFF'],  // Bar colors
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            indexAxis: 'y',  // Horizontal bar chart
            plugins: {
                legend: { display: false }  // Hide legend
            },
            scales: {
                x: {
                    ticks: {
                        stepSize: 1,  // Defines the interval between tick marks on the x-axis
                        callback: function(value) {
                            // Ensure that the ticks are shown as integers (no decimal places)
                            return Number.isInteger(value) ? value : value.toFixed(0);
                        }
                    }
                }
            }
        }
    });
});

const donutCtx = document.getElementById('donutChart').getContext('2d');
let donutChart = new Chart(donutCtx, {
    type: 'pie',
    data: {
        labels: Object.keys(version0Data), // District names (keys from the map)
        datasets: [{
            data: Object.values(version0Data), // Counts for version 0 (values from the map)
            backgroundColor: [
                '#004b75', '#007a8e', '#009999', '#00a8c9', '#2dbab0', '#3ddbd4',
                '#72e1c5', '#ade6e0', '#95d8eb', '#c2f1ff' // Custom colors for each district
            ]
        }]
    },
    options: {
        maintainAspectRatio: false,
        responsive: true,
        plugins: {
            legend: {
                display: true,
                position: 'bottom',
                labels: {
                    font: { size: 12 }
                }
            }
        }
    }
});