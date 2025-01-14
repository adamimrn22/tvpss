// Bar Chart

function updateBarChart(barChartData) {
    const barCtx = document.getElementById('barChart').getContext('2d');
    const currentYear = new Date().getFullYear();

    new Chart(barCtx, {
        type: 'bar',
        data: barChartData, // Dynamic data passed from Thymeleaf
        options: {
            responsive: true,
            maintainAspectRatio: false,
            indexAxis: 'x',
            plugins: {
                legend: { display: false }
            },
            scales: {
                y: {
                    ticks: {
                        callback: function(value) {
                            return Number.isInteger(value) ? value : '';  // Display only whole numbers
                        }
                    }
                }
            }
        }
    });
}


function updateDonutChart(maleCount, femaleCount) {
    const donutCtx = document.getElementById('donutChart').getContext('2d');
    new Chart(donutCtx, {
        type: 'pie',
        data: {
            labels: ['Lelaki', 'Perempuan'],
            datasets: [{
                data: [maleCount, femaleCount], // Use dynamic values here
                backgroundColor: ['#25565B', '#2EA7A6']
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
                }
            }
        }
    });
}