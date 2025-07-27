using System.Drawing;
using System.Windows.Forms;

namespace client
{
    partial class Reports
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            label1 = new Label();
            label2 = new Label();
            comboTrial = new ComboBox();
            viewParticipant = new DataGridView();
            NameColumn = new DataGridViewTextBoxColumn();
            CodeColumn = new DataGridViewTextBoxColumn();
            PointsColumn = new DataGridViewTextBoxColumn();
            button1 = new Button();
            ((System.ComponentModel.ISupportInitialize)viewParticipant).BeginInit();
            SuspendLayout();
            // 
            // label1
            // 
            label1.AutoSize = true;
            label1.Font = new Font("Arial", 13.8F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label1.Location = new Point(30, 30);
            label1.Name = "label1";
            label1.Size = new Size(93, 26);
            label1.TabIndex = 0;
            label1.Text = "Reports";
            // 
            // label2
            // 
            label2.AutoSize = true;
            label2.Font = new Font("Arial", 12F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label2.Location = new Point(30, 80);
            label2.Name = "label2";
            label2.Size = new Size(93, 23);
            label2.TabIndex = 1;
            label2.Text = "Filter by: ";
            // 
            // comboTrial
            // 
            comboTrial.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            comboTrial.FormattingEnabled = true;
            comboTrial.Location = new Point(116, 80);
            comboTrial.Name = "comboTrial";
            comboTrial.Size = new Size(200, 27);
            comboTrial.TabIndex = 2;
            comboTrial.Text = "Choose a trial";
            comboTrial.SelectedIndexChanged += comboTrial_SelectedIndexChanged;
            // 
            // viewParticipant
            // 
            viewParticipant.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            viewParticipant.Columns.AddRange(new DataGridViewColumn[] { NameColumn, CodeColumn, PointsColumn });
            viewParticipant.Location = new Point(30, 124);
            viewParticipant.Name = "viewParticipant";
            viewParticipant.RowHeadersWidth = 51;
            viewParticipant.Size = new Size(640, 297);
            viewParticipant.TabIndex = 3;
            // 
            // NameColumn
            // 
            NameColumn.HeaderText = "Name";
            NameColumn.MinimumWidth = 6;
            NameColumn.Name = "NameColumn";
            NameColumn.Width = 225;
            // 
            // CodeColumn
            // 
            CodeColumn.HeaderText = "Code";
            CodeColumn.MinimumWidth = 6;
            CodeColumn.Name = "CodeColumn";
            CodeColumn.Width = 100;
            // 
            // PointsColumn
            // 
            PointsColumn.HeaderText = "Points";
            PointsColumn.MinimumWidth = 6;
            PointsColumn.Name = "PointsColumn";
            PointsColumn.Width = 100;
            // 
            // button1
            // 
            button1.Font = new Font("Arial", 10.8F, FontStyle.Regular, GraphicsUnit.Point, 0);
            button1.Location = new Point(576, 80);
            button1.Name = "button1";
            button1.Size = new Size(94, 40);
            button1.TabIndex = 4;
            button1.Text = "Close";
            button1.UseVisualStyleBackColor = true;
            button1.Click += button1_Click;
            // 
            // Reports
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(702, 433);
            Controls.Add(button1);
            Controls.Add(viewParticipant);
            Controls.Add(comboTrial);
            Controls.Add(label2);
            Controls.Add(label1);
            Name = "Reports";
            Text = "Reports";
            Load += Reports_Load;
            ((System.ComponentModel.ISupportInitialize)viewParticipant).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Label label1;
        private Label label2;
        private ComboBox comboTrial;
        private DataGridView viewParticipant;
        private Button button1;
        private DataGridViewTextBoxColumn NameColumn;
        private DataGridViewTextBoxColumn CodeColumn;
        private DataGridViewTextBoxColumn PointsColumn;
    }
}