function initMatchesPage() {
    console.log("Initializing Matches Page...");

    const matchesTbody = document.getElementById('matches-tbody');
    const paginationControls = document.getElementById('pagination-controls');
    const filterInput = document.getElementById('filter-input');
    const resetFilterBtn = document.getElementById('reset-filter-btn');
    const searchBtn = document.getElementById('search-btn');

    let currentFilter = '';

    async function loadMatches(page = 1, filterQuery = '') {
        try {
            const contextPath = window.contextPath || '';
            let url = `${contextPath}/api/matches?page=${page}&pageSize=5`;
            if (filterQuery) {
                url += `&filter_by_player_name=${encodeURIComponent(filterQuery)}`;
            }
            const response = await fetch(url, {
                headers: {
                    'Accept': 'application/json'
                }
            });
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

    function performSearch() {
        const query = filterInput.value.trim();
        currentFilter = query;
        loadMatches(1, currentFilter); // Загружаем первую страницу с новым фильтром
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

            loadMatches(page, currentFilter);
        }
    });

    filterInput.addEventListener('keyup', (event) => {
        if (event.key === 'Enter') {
            performSearch();
        }
    });

    searchBtn.addEventListener('click', (event) => {
        event.preventDefault();
        performSearch();
    });

    resetFilterBtn.addEventListener('click', (event) => {
        event.preventDefault();
        filterInput.value = '';
        currentFilter = '';
        loadMatches(1, currentFilter);
    });
    loadMatches(1, currentFilter);
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
