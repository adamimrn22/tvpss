
document.querySelectorAll('.floating-select').forEach(select => {
    select.addEventListener('change', function () {
        this.setAttribute('value', this.value);
    });
});

// Bar Chart
const barCtx = document.getElementById('barChart').getContext('2d');
new Chart(barCtx, {
    type: 'bar',
    data: {
        labels: ['Admin State', 'Admin PPD', 'Admin Sekolah'],
        datasets: [{
            label: 'Bilangan Pengguna',
            data: [60, 120, 300],
            backgroundColor: ['#25565B', '#2ECAFF', '#228FA6']
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        indexAxis: 'y',
        plugins: {
            legend: { display: false }
        }
    }
});


// Line Chart
const lineCtx = document.getElementById('lineChart').getContext('2d');
new Chart(lineCtx, {
    type: 'line',
    data: {
        labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24'],
        datasets: [
            {
                label: 'Admin State Login',
                data: [100, 200, 150, 300, 250, 400, 300, 0, 0, 0, 0, 0, 0, 0, 0, 23, 22, 21, 0, 0, 0, 0, 0, 0],
                borderColor: '#37285C',
                borderWidth: 2,
                fill: false
            },
            {
                label: 'Admin School Login',
                data: [500, 1500, 3000, 2500, 1000, 2000, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                borderColor: '#94FFCC',
                borderWidth: 2,
                fill: false
            },
            {
                label: 'Admin PPD Login',
                data: [200, 300, 1000, 800, 400, 600, 500, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                borderColor: '#0096BF',
                borderWidth: 2,
                fill: false
            }
        ]
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

// Donut Chart
const donutCtx = document.getElementById('donutChart').getContext('2d');
new Chart(donutCtx, {
    type: 'pie',
    data: {
        labels: ['Admin State', 'Admin PPD', 'Admin Sekolah'],
        datasets: [{
            data: [10, 50, 40],
            backgroundColor: ['#003F5C', '#FFA600', '#BC5090']
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
