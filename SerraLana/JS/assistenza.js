//Navbar
window.addEventListener('scroll', function() {
      const header = document.getElementById('navbar');
          if (window.scrollY > 50) {
          header.classList.add('scrolled');
          } else {
      header.classList.remove('scrolled');
      }
  });



  // FAQ
  document.querySelectorAll('.faq-question').forEach(question => {
    question.addEventListener('click', function () {
        document.querySelectorAll('.faq-question').forEach(q => {
            if (q !== this) {
                q.classList.remove('active');
                q.nextElementSibling.style.display = 'none';
            }
        });
        
        this.classList.toggle('active');
        let answer = this.nextElementSibling;
        answer.style.display = answer.style.display === 'block' ? 'none' : 'block';
    });
});