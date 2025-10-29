// Funzioni globali per il filtro
window.applyClientFilter = function() {
    const txtSearchWidget = zk.Widget.$('$txtSearch');
    const ddlFieldWidget = zk.Widget.$('$ddlField');
    const gridWidget = zk.Widget.$('$gridProfili');

    if (!txtSearchWidget || !ddlFieldWidget || !gridWidget) return;

    const searchValue = (txtSearchWidget.getValue() || '').trim().toLowerCase();
    const fieldValue = (ddlFieldWidget.getValue() || 'Nome Abilitazione').toLowerCase();
    if (!searchValue) { resetClientFilter(); return; }

    const fieldMap = {
        'nome abilitazione': 'col-nome-cell',
        'aggregato': 'col-aggregato-cell',
        'tipologia': 'col-tipologia-cell',
        'descrizione (breve)': 'col-desc-cell',
        'sistema target': 'col-sistema-cell'
    };
    const cellClass = fieldMap[fieldValue] || 'col-nome-cell';
    const rows = gridWidget.$n().querySelectorAll('tbody tr.z-row');

    rows.forEach(row => {
        const cell = row.querySelector('.' + cellClass);
        const label = cell ? cell.querySelector('label') : null;
        const text = label ? (label.textContent || '').toLowerCase() : '';
        row.style.display = text.indexOf(searchValue) !== -1 ? '' : 'none';
    });
};

window.resetClientFilter = function() {
    const gridWidget = zk.Widget.$('$gridProfili');
    if (!gridWidget) return;
    const rows = gridWidget.$n().querySelectorAll('tbody tr.z-row');
    rows.forEach(row => row.style.display = '');
};

// Scroll indicator
document.addEventListener('DOMContentLoaded', function() {
    const wrapper = document.getElementById('tableScrollWrapper');
    const leftIndicator = document.getElementById('scrollLeft');
    if(wrapper) wrapper.addEventListener('scroll', () => {
        if(leftIndicator) leftIndicator.style.display = wrapper.scrollLeft > 50 ? 'block' : 'none';
    });

    // Limit paging buttons e observer per aggiornamenti dinamici
    function limitPagingButtons() { /* copia la logica originale */ }
    setTimeout(limitPagingButtons, 300);
});
