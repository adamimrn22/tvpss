// Bar Chart
const barCtx = document.getElementById('barPPDChart').getContext('2d');
new Chart(barCtx, {
    type: 'bar',
    data: {
        labels: ['Versi 0', 'Versi 1', 'Versi 2', 'Versi 3', 'Versi 4'],
        datasets: [{
            label: 'Bilangan Sekolah',
            data: [
                schoolVersion0Counts,
                schoolVersion1Counts,
                schoolVersion2Counts,
                schoolVersion3Counts,
                schoolVersion4Counts
            ],
            backgroundColor: ['#25565B', '#2ECAFF', '#228FA6', '#2EA7A6', '#4A94B4']
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        indexAxis: 'y',
        plugins: {
            legend: { display: false }
        },
        scales: {
            x: {
                ticks: {
                    // This will ensure the x-axis ticks are displayed as whole numbers without decimals
                    stepSize: 1,  // This makes the steps whole numbers
                    callback: function(value) {
                        return Number.isInteger(value) ? value : value.toFixed(0);  // Ensure integer formatting
                    }
                }
            }
        }
    }
});

const donutCtx = document.getElementById('donutPPDChart').getContext('2d');
new Chart(donutCtx, {
    type: 'pie',  // Donut chart is a pie chart with a hole in the center
    data: {
        labels: ['Versi 0', 'Versi 1', 'Versi 2', 'Versi 3', 'Versi 4'],
        datasets: [{
            data: [
                version0Percentage,
                version1Percentage,
                version2Percentage,
                version3Percentage,
                version4Percentage
            ],
            backgroundColor: ['#25565B', '#2EA7A6', '#48FFEA', '#2ECAFF', '#4A94B4']
        }]
    },
    options: {
        maintainAspectRatio: false,
        responsive: true,
        plugins: {
            legend: {
                display: true,
                position: 'bottom', // Move labels below the chart
                labels: {
                    font: {
                        size: 12 // Adjust label font size if needed
                    }
                }
            },
            tooltip: {
                callbacks: {
                    label: function(tooltipItem) {
                        var value = tooltipItem.raw.toFixed(2); // Limit to 2 decimal places
                        return value + '%'; // Add percentage sign to the tooltip label
                    }
                }
            }
        }
    }
});