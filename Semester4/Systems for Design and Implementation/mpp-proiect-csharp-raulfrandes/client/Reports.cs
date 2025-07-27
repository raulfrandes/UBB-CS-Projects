using System;
using System.Collections.Generic;
using System.Windows.Forms;
using model.DTOs;

namespace client;

public partial class Reports : Form
{
    private readonly TriathlonController _controller;
    private IList<ParticipantDTO> _participantData;

    public Reports(TriathlonController controller)
    {
        InitializeComponent();
        _controller = controller;
        _participantData = new List<ParticipantDTO>();
        _controller.updateEvent += UpdateUser;
    }

    private void Reports_Load(object sender, EventArgs e)
    {
        InitComboBox();
    }

    private void InitComboBox()
    {
        var trialNames = _controller.LoadTrials();
        comboTrial.Items.Clear();
        foreach (var trial in trialNames) comboTrial.Items.Add(trial);
    }

    private void LoadParticipants()
    {
        var selectedTrial = comboTrial.SelectedItem as TrialDTO;
        if (selectedTrial == null)
        {
            MessageBox.Show(@"Select a trial!");
        }
        else
        {
            var participants = _controller.LoadParticipantsFiltered(selectedTrial.Id);
            viewParticipant.Rows.Clear();
            foreach (var participant in participants)
            {
                var rowIndex = viewParticipant.Rows.Add(participant);
                var row = viewParticipant.Rows[rowIndex];
                row.Cells["NameColumn"].Value = participant.Name;
                row.Cells["CodeColumn"].Value = participant.Code;
                row.Cells["PointsColumn"].Value = participant.Points.ToString();
            }
        }
    }

    private void comboTrial_SelectedIndexChanged(object sender, EventArgs e)
    {
        LoadParticipants();
        _controller.HandleSendFilter((comboTrial.SelectedItem as TrialDTO)!.Id);
    }

    private void button1_Click(object sender, EventArgs e)
    {
        Close();
        _controller.updateEvent -= UpdateUser;
    }

    public void UpdateUser(object sender, UserEventArgs e)
    {
        if (e.UserEvent == UserEvent.PointsReceivedFiltered)
        {
            _participantData = (List<ParticipantDTO>)e.Data;
            Console.WriteLine(@"[Reports] receive points filtered");
            viewParticipant.BeginInvoke(new UpdateDataGridViewCallback(UpdateDataGridView),
                new Object[] { viewParticipant, _participantData });
        }
    }

    private void UpdateDataGridView(DataGridView dataGridView, IList<ParticipantDTO> newData)
    {
        viewParticipant.DataSource = null;
        viewParticipant.Rows.Clear();
        foreach (ParticipantDTO participant in newData)
        {
            var rowIndex = viewParticipant.Rows.Add(participant);
            var row = viewParticipant.Rows[rowIndex];
            row.Cells["NameColumn"].Value = participant.Name;
            row.Cells["CodeColumn"].Value = participant.Code;
            row.Cells["PointsColumn"].Value = participant.Points.ToString();
        }
    }

    public delegate void UpdateDataGridViewCallback(DataGridView dataGridView, IList<ParticipantDTO> data);
}