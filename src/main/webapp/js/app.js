function initMatchesPage() {
    console.log("Initializing Matches Page...");

    const matchesTbody = document.getElementById('matches-tbody');
    const paginationControls = document.getElementById('pagination-controls');

    async function loadMatches(page = 1) {
        try {
            const url = `/tennis_scoreboard_main_war_exploded/api/matches?page=${page}&pageSize=5`;
            const response = await fetch(url);
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            const data = await response.json();
            renderTable(data.content);
            renderPagination(data);
        } catch (error) {
            console.error("Could not fetch matches:", error);
            matchesTbody.innerHTML = '<tr><td colspan="3">Failed to load data. Please try again later.</td></tr>';
        }
    }

    function renderTable(matches) {
        matchesTbody.innerHTML = '';

        if (matches.length === 0) {
            matchesTbody.innerHTML = '<tr><td colspan="3">No matches found.</td></tr>';
            return;
        }

        matches.forEach(match => {
            const winnerName = match.winner ? match.winner.name : '—';
            const rowHtml = `
                    <tr>
                        <td>${match.player1.name}</td>
                        <td>${match.player2.name}</td>
                        <td><span class="winner-name-td">${winnerName}</span></td>
                    </tr>
                `;
            matchesTbody.insertAdjacentHTML('beforeend', rowHtml);
        });
    }

    function renderPagination(data) {
        paginationControls.innerHTML = '';
        const {currentPage, totalPages} = data;

        if (currentPage > 1) {
            paginationControls.insertAdjacentHTML('beforeend', `<a class="prev" href="#" data-page="${currentPage - 1}"> &lt; </a>`);
        }
        for (let i = 1; i <= totalPages; i++) {
            const currentClass = (i === currentPage) ? 'current' : '';
            paginationControls.insertAdjacentHTML('beforeend', `<a class="num-page ${currentClass}" href="#" data-page="${i}">${i}</a>`);
        }
        if (currentPage < totalPages) {
            paginationControls.insertAdjacentHTML('beforeend', `<a class="next" href="#" data-page="${currentPage + 1}"> &gt; </a>`);
        }
    }

    paginationControls.addEventListener('click', (event) => {
        event.preventDefault();
        const target = event.target.closest('a[data-page]');
        if (target) {
            const page = parseInt(target.dataset.page, 10);
            loadMatches(page);
        }
    });

    loadMatches(1);
}

function initNewMatchPage() {
    console.log("Initializing New Match Page...");

}

document.addEventListener("DOMContentLoaded", function () {

    const navToggle = document.querySelector(".nav-toggle");
    const navLinks = document.querySelector(".nav-links");

    if (navToggle && navLinks) {
        navToggle.addEventListener("click", function () {
            navLinks.classList.toggle("active");
        });
    }

    if (document.getElementById('matches-tbody')) {
        initMatchesPage();
    }

    if (document.getElementById('new-match-form')) {
        initNewMatchPage();
    }
});
