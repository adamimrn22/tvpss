// Bar Chart
const barCtx = document.getElementById('barChart').getContext('2d');
new Chart(barCtx, {
    type: 'bar',
    data: {
        labels: ['Versi 1', 'Versi 2', 'Versi 3', 'Versi 4'],
        datasets: [{
            label: 'Bilangan Pengguna',
            data: [60, 120, 300, 23],
            backgroundColor: ['#25565B', '#2ECAFF', '#228FA6', '#2EA7A6']
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

// Donut Chart
const donutCtx = document.getElementById('donutChart').getContext('2d');
new Chart(donutCtx, {
    type: 'pie',
    data: {
        labels: ['Versi 1', 'Versi 2', 'Versi 3', 'Versi 4'],
        datasets: [{
            data: [5, 50, 40, 3],
            backgroundColor: ['#25565B', '#2EA7A6', '#48FFEA', '2ECAFF']
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