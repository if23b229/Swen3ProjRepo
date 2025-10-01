async function load() {
    const res = await fetch('/api/v1/messages');
    const data = await res.json();
    const ul = document.getElementById('list');
    ul.innerHTML = '';
    data.forEach(m => {
        const li = document.createElement('li');
        li.textContent = `${m.id ?? 'no-id'} | ${m.text} — ${m.author}`;
        ul.appendChild(li);
    });
}

document.getElementById('refresh').onclick = load;

document.getElementById('msgForm').onsubmit = async (e) => {
    e.preventDefault();
    const text = document.getElementById('text').value;
    const author = document.getElementById('author').value;
    await fetch('/api/v1/messages', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ text, author })
    });
    document.getElementById('text').value = '';
    document.getElementById('author').value = '';
    load();
};

load();