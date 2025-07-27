using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using model;
using model.DTOs;
using services;

namespace client
{
    public partial class RefereeProfile : Form
    {
        private readonly TriathlonController _controller;
        private Referee _referee;
        private IList<ParticipantDTO> _participantsData;

        public RefereeProfile(TriathlonController controller, Referee referee)
        {
            InitializeComponent();
            _controller = controller;
            _referee = referee;
            _participantsData = _controller.LoadParticipants();
            LoadParticipants();
            InitComboBox();
            _controller.updateEvent += UserUpdate;
        }

        private void RefereeProfile_Load(object sender, EventArgs e)
        {
            refereeLabel.Text = refereeLabel.Text + _referee.Name + @"!";
        }

        private void LoadParticipants()
        {
            viewParticipants.Rows.Clear();
            foreach (ParticipantDTO participant in _participantsData)
            {
                int rowIndex = viewParticipants.Rows.Add();
                DataGridViewRow row = viewParticipants.Rows[rowIndex];
                row.Cells["NameColumn"].Value = participant.Name;
                row.Cells["CodeColumn"].Value = participant.Code;
                row.Cells["PointsColumn"].Value = participant.Points.ToString();
                row.Tag = participant.Id;
            }
        }

        private void InitComboBox()
        {
            List<TrialDTO> trialNames = _controller.LoadTrials();
            comboTrial.Items.Clear();
            foreach (TrialDTO trial in trialNames)
            {
                comboTrial.Items.Add(trial);
            }
        }

        private void viewParticipants_SelectionChanged(object sender, EventArgs e)
        {
            if (viewParticipants.SelectedRows.Count > 0)
            {
                DataGridViewRow selectedRow = viewParticipants.SelectedRows[0];
                txtName.Text = selectedRow.Cells["NameColumn"].Value.ToString();
                txtCode.Text = selectedRow.Cells["CodeColumn"].Value.ToString();
            }
            else
            {
                txtName.Text = "";
                txtCode.Text = "";
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            DataGridViewRow selectedRow = viewParticipants.SelectedRows[0];
            if (selectedRow == null)
            {
                MessageBox.Show(@"Select a participant!");
            }
            else
            {
                TrialDTO? selectedTrial = comboTrial.SelectedItem as TrialDTO;
                if (selectedTrial == null)
                {
                    MessageBox.Show(@"Select a trial!");
                }
                else
                {
                    try
                    {
                        int points = int.Parse(txtPoints.Text);
                        _controller.HandleAddPoints((long)selectedRow.Tag!, selectedTrial.Id, points);
                        MessageBox.Show(@"Points added to " + selectedRow.Cells["NameColumn"].Value + @" for the " + selectedTrial.Name + @" trial");
                        txtName.Text = "";
                        txtCode.Text = "";
                        txtPoints.Text = "";
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show(ex.Message);
                    }
                }
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Form reportsForm = new Reports(_controller);
            reportsForm.Show();
        }

        /*private void RefereeProfile_FormClosing(object sender, FormClosingEventArgs e)
        {
            Console.WriteLine(@"RefereeProfile closing " + e.CloseReason);
            if (e.CloseReason == CloseReason.ApplicationExitCall)
            {
                Console.WriteLine("hello");
                _controller.HandleLogout();
                _controller.updateEvent -= UserUpdate;
            }
        }*/

        private void button3_Click(object sender, EventArgs e)
        {
            try
            {
                _controller.HandleLogout();
            }
            catch (ServiceException ex)
            {
                MessageBox.Show(ex.Message);
            }

            _controller.updateEvent -= UserUpdate;
            Application.Exit();  
        }

        public void UserUpdate(object sender, UserEventArgs e)
        {
            if (e.UserEvent == UserEvent.PointsReceived)
            {
                _participantsData = (List<ParticipantDTO>) e.Data;
                Console.WriteLine(@"[RefereeProfile] receive points ");
                viewParticipants.BeginInvoke(new UpdateDataGridViewCallback(UpdateDataGridView),
                    new Object[] { viewParticipants, _participantsData });
            }
        }

        private void UpdateDataGridView(DataGridView dataGridView, IList<ParticipantDTO> newData)
        {
            viewParticipants.DataSource = null;
            viewParticipants.Rows.Clear();
            foreach (ParticipantDTO participant in newData)
            {
                int rowIndex = viewParticipants.Rows.Add();
                DataGridViewRow row = viewParticipants.Rows[rowIndex];
                row.Cells["NameColumn"].Value = participant.Name;
                row.Cells["CodeColumn"].Value = participant.Code;
                row.Cells["PointsColumn"].Value = participant.Points.ToString();
                row.Tag = participant.Id;
            }
        }

        public delegate void UpdateDataGridViewCallback(DataGridView dataGridView, IList<ParticipantDTO> data);
    }
}
