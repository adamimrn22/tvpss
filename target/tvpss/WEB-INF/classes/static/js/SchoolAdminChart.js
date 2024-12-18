// Bar Chart
const barCtx = document.getElementById('barChart').getContext('2d');
const currentYear = new Date().getFullYear();

new Chart(barCtx, {
    type: 'bar',

    data: {
        labels: [
            `${currentYear - 4}`,
            `${currentYear - 3}`,
            `${currentYear - 2}`,
            `${currentYear - 1}`,
            `${currentYear}`
        ],
        datasets: [{
            data: [10, 15, 4, 10, 20], // Example data for each year
            backgroundColor: [
                '#004b75', // Dark Blue
                '#007a8e', // Teal
                '#009999', // Aquamarine
                '#00a8c9', // Sky Blue
                '#72e1c5'  // Light Green
            ]
        }]
    },

    options: {
        responsive: true,
        maintainAspectRatio: false,
        indexAxis: 'x',
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
        labels: ['Lelaki', 'Perempuan'],
        datasets: [{
            data: [25, 23],
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