class TabManager {
  constructor(...tabs) {
    this.tabs = [...tabs];
    console.log(this.tabs);
  }

  showTab(tab) {
    if (this.tabs.length > 0) {
      this.tabs.forEach(curTab => {
        const tabElement = document.getElementById(curTab);
        if (tabElement) {
          if (curTab === tab) {
            tabElement.style.display = 'block';
            document.getElementById(curTab + "_tab").classList.add('active');
          } else {
            tabElement.style.display = 'none';
            document.getElementById(curTab + "_tab").classList.remove('active');
          }
        }
      });
    }
  }
}
