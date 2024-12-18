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
        labels: [
            'Batu Pahat',
            'Johor Bahru',
            'Kluang',
            'Kota Tinggi',
            'Kulai',
            'Mersing',
            'Muar',
            'Pontian',
            'Segamat',
            'Tangkak'
        ],
        datasets: [{
            data: [15, 25, 10, 8, 12, 6, 20, 18, 14, 9], // Example data for each district
            backgroundColor: [
                '#004b75', // Batu Pahat - Dark Blue
                '#007a8e', // Johor Bahru - Teal
                '#009999', // Kluang - Aquamarine
                '#00a8c9', // Kota Tinggi - Sky Blue
                '#2dbab0', // Kulai - Mint Green
                '#3ddbd4', // Mersing - Light Turquoise
                '#72e1c5', // Muar - Light Green
                '#ade6e0', // Pontian - Pastel Aqua
                '#95d8eb', // Segamat - Soft Blue
                '#c2f1ff'  // Tangkak - Very Light Blue
            ]
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