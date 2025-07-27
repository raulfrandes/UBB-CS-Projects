using System.Drawing;
using System.Windows.Forms;

namespace client
{
    partial class RefereeProfile
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
            refereeLabel = new Label();
            viewParticipants = new DataGridView();
            NameColumn = new DataGridViewTextBoxColumn();
            CodeColumn = new DataGridViewTextBoxColumn();
            PointsColumn = new DataGridViewTextBoxColumn();
            groupBox1 = new GroupBox();
            button1 = new Button();
            comboTrial = new ComboBox();
            txtPoints = new TextBox();
            txtCode = new TextBox();
            txtName = new TextBox();
            label5 = new Label();
            label4 = new Label();
            label3 = new Label();
            label2 = new Label();
            button2 = new Button();
            button3 = new Button();
            ((System.ComponentModel.ISupportInitialize)viewParticipants).BeginInit();
            groupBox1.SuspendLayout();
            SuspendLayout();
            // 
            // refereeLabel
            // 
            refereeLabel.AutoSize = true;
            refereeLabel.Font = new Font("Arial", 13.8F, FontStyle.Regular, GraphicsUnit.Point, 0);
            refereeLabel.Location = new Point(30, 30);
            refereeLabel.Name = "refereeLabel";
            refereeLabel.Size = new Size(168, 26);
            refereeLabel.TabIndex = 0;
            refereeLabel.Text = "Welcome back ";
            // 
            // viewParticipants
            // 
            viewParticipants.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            viewParticipants.Columns.AddRange(new DataGridViewColumn[] { NameColumn, CodeColumn, PointsColumn });
            viewParticipants.Location = new Point(30, 89);
            viewParticipants.Name = "viewParticipants";
            viewParticipants.RowHeadersWidth = 51;
            viewParticipants.Size = new Size(683, 400);
            viewParticipants.TabIndex = 1;
            viewParticipants.SelectionChanged += viewParticipants_SelectionChanged;
            // 
            // NameColumn
            // 
            NameColumn.HeaderText = "Name";
            NameColumn.MinimumWidth = 6;
            NameColumn.Name = "NameColumn";
            NameColumn.Width = 258;
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
            // groupBox1
            // 
            groupBox1.Controls.Add(button1);
            groupBox1.Controls.Add(comboTrial);
            groupBox1.Controls.Add(txtPoints);
            groupBox1.Controls.Add(txtCode);
            groupBox1.Controls.Add(txtName);
            groupBox1.Controls.Add(label5);
            groupBox1.Controls.Add(label4);
            groupBox1.Controls.Add(label3);
            groupBox1.Controls.Add(label2);
            groupBox1.Font = new Font("Arial", 10.8F, FontStyle.Bold, GraphicsUnit.Point, 0);
            groupBox1.Location = new Point(747, 89);
            groupBox1.Name = "groupBox1";
            groupBox1.RightToLeft = RightToLeft.No;
            groupBox1.Size = new Size(325, 400);
            groupBox1.TabIndex = 2;
            groupBox1.TabStop = false;
            groupBox1.Text = "Add Points";
            // 
            // button1
            // 
            button1.Font = new Font("Arial", 10.8F, FontStyle.Regular, GraphicsUnit.Point, 0);
            button1.Location = new Point(100, 346);
            button1.Name = "button1";
            button1.Size = new Size(125, 40);
            button1.TabIndex = 8;
            button1.Text = "Add points";
            button1.UseVisualStyleBackColor = true;
            button1.Click += button1_Click;
            // 
            // comboTrial
            // 
            comboTrial.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            comboTrial.FormattingEnabled = true;
            comboTrial.Location = new Point(25, 209);
            comboTrial.Name = "comboTrial";
            comboTrial.Size = new Size(275, 27);
            comboTrial.TabIndex = 7;
            comboTrial.Text = "Choose a Trial";
            // 
            // txtPoints
            // 
            txtPoints.Font = new Font("Arial", 10.2F);
            txtPoints.Location = new Point(25, 283);
            txtPoints.Name = "txtPoints";
            txtPoints.Size = new Size(275, 27);
            txtPoints.TabIndex = 6;
            // 
            // txtCode
            // 
            txtCode.Font = new Font("Arial", 10.2F);
            txtCode.Location = new Point(25, 137);
            txtCode.Name = "txtCode";
            txtCode.Size = new Size(275, 27);
            txtCode.TabIndex = 5;
            // 
            // txtName
            // 
            txtName.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            txtName.Location = new Point(25, 66);
            txtName.Name = "txtName";
            txtName.Size = new Size(275, 27);
            txtName.TabIndex = 4;
            // 
            // label5
            // 
            label5.AutoSize = true;
            label5.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label5.Location = new Point(25, 261);
            label5.Name = "label5";
            label5.Size = new Size(54, 19);
            label5.TabIndex = 3;
            label5.Text = "Points";
            // 
            // label4
            // 
            label4.AutoSize = true;
            label4.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label4.Location = new Point(25, 187);
            label4.Name = "label4";
            label4.Size = new Size(39, 19);
            label4.TabIndex = 2;
            label4.Text = "Trial";
            // 
            // label3
            // 
            label3.AutoSize = true;
            label3.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label3.Location = new Point(25, 115);
            label3.Name = "label3";
            label3.Size = new Size(48, 19);
            label3.TabIndex = 1;
            label3.Text = "Code";
            // 
            // label2
            // 
            label2.AutoSize = true;
            label2.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label2.Location = new Point(25, 44);
            label2.Name = "label2";
            label2.Size = new Size(51, 19);
            label2.TabIndex = 0;
            label2.Text = "Name";
            // 
            // button2
            // 
            button2.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            button2.Location = new Point(30, 521);
            button2.Name = "button2";
            button2.Size = new Size(94, 40);
            button2.TabIndex = 3;
            button2.Text = "Reports";
            button2.UseVisualStyleBackColor = true;
            button2.Click += button2_Click;
            // 
            // button3
            // 
            button3.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            button3.Location = new Point(978, 521);
            button3.Name = "button3";
            button3.Size = new Size(94, 40);
            button3.TabIndex = 4;
            button3.Text = "Logout";
            button3.UseVisualStyleBackColor = true;
            button3.Click += button3_Click;
            // 
            // RefereeProfile
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(1100, 578);
            Controls.Add(button3);
            Controls.Add(button2);
            Controls.Add(groupBox1);
            Controls.Add(viewParticipants);
            Controls.Add(refereeLabel);
            Name = "RefereeProfile";
            Text = "RefereeProfile";
            Load += RefereeProfile_Load;
            ((System.ComponentModel.ISupportInitialize)viewParticipants).EndInit();
            groupBox1.ResumeLayout(false);
            groupBox1.PerformLayout();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Label refereeLabel;
        private DataGridView viewParticipants;
        private GroupBox groupBox1;
        private Button button1;
        private ComboBox comboTrial;
        private TextBox txtPoints;
        private TextBox txtCode;
        private TextBox txtName;
        private Label label5;
        private Label label4;
        private Label label3;
        private Label label2;
        private Button button2;
        private Button button3;
        private DataGridViewTextBoxColumn NameColumn;
        private DataGridViewTextBoxColumn CodeColumn;
        private DataGridViewTextBoxColumn PointsColumn;
    }
}