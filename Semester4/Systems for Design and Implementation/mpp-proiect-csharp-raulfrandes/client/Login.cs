using System;
using System.Windows.Forms;

namespace client
{
    public partial class Login : Form
    {
        private TriathlonController _controller;
        
        public Login(TriathlonController controller)
        {
            InitializeComponent();
            _controller = controller;
        }

        private void loginButton_Click(object sender, EventArgs e)
        {
            String username = txtUsername.Text;
            string password = txtPassword.Text;
            txtUsername.Clear();
            txtPassword.Clear();
            if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password))
            {
                MessageBox.Show(@"Please enter username and/or password!");
            }
            else
            {
                try
                {
                    _controller.HandleLogin(username, password);
                    Hide();
                } catch (Exception exception)
                {
                    MessageBox.Show(exception.Message);
                }
            }
        }
    }
}
