namespace CinemaAppGeneric
{
    partial class Form1
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
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
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            viewParent = new DataGridView();
            parentLabel = new Label();
            viewChild = new DataGridView();
            childLabel = new Label();
            adaugaButton = new Button();
            stergeButton = new Button();
            actualizeazaButton = new Button();
            childPanel = new Panel();
            ((System.ComponentModel.ISupportInitialize)viewParent).BeginInit();
            ((System.ComponentModel.ISupportInitialize)viewChild).BeginInit();
            SuspendLayout();
            // 
            // viewParent
            // 
            viewParent.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            viewParent.Location = new Point(12, 344);
            viewParent.Name = "viewParent";
            viewParent.RowHeadersWidth = 51;
            viewParent.Size = new Size(325, 257);
            viewParent.TabIndex = 1;
            // 
            // parentLabel
            // 
            parentLabel.AutoSize = true;
            parentLabel.Font = new Font("Arial", 10.8F, FontStyle.Regular, GraphicsUnit.Point, 0);
            parentLabel.Location = new Point(12, 320);
            parentLabel.Name = "parentLabel";
            parentLabel.Size = new Size(63, 21);
            parentLabel.TabIndex = 2;
            parentLabel.Text = "Parent";
            // 
            // viewChild
            // 
            viewChild.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            viewChild.Location = new Point(12, 38);
            viewChild.Name = "viewChild";
            viewChild.RowHeadersWidth = 51;
            viewChild.Size = new Size(928, 257);
            viewChild.TabIndex = 3;
            viewChild.SelectionChanged += viewChild_SelectionChanged;
            // 
            // childLabel
            // 
            childLabel.AutoSize = true;
            childLabel.Font = new Font("Arial", 10.8F, FontStyle.Regular, GraphicsUnit.Point, 0);
            childLabel.Location = new Point(12, 14);
            childLabel.Name = "childLabel";
            childLabel.Size = new Size(51, 21);
            childLabel.TabIndex = 4;
            childLabel.Text = "Child";
            // 
            // adaugaButton
            // 
            adaugaButton.Location = new Point(387, 529);
            adaugaButton.Name = "adaugaButton";
            adaugaButton.Size = new Size(111, 29);
            adaugaButton.TabIndex = 11;
            adaugaButton.Text = "Adauga";
            adaugaButton.UseVisualStyleBackColor = true;
            adaugaButton.Click += adaugaButton_Click;
            // 
            // stergeButton
            // 
            stergeButton.Location = new Point(598, 529);
            stergeButton.Name = "stergeButton";
            stergeButton.Size = new Size(111, 29);
            stergeButton.TabIndex = 12;
            stergeButton.Text = "Sterge";
            stergeButton.UseVisualStyleBackColor = true;
            stergeButton.Click += stergeButton_Click;
            // 
            // actualizeazaButton
            // 
            actualizeazaButton.Location = new Point(809, 529);
            actualizeazaButton.Name = "actualizeazaButton";
            actualizeazaButton.Size = new Size(111, 29);
            actualizeazaButton.TabIndex = 19;
            actualizeazaButton.Text = "Actualizeaza";
            actualizeazaButton.UseVisualStyleBackColor = true;
            actualizeazaButton.Click += actualizeazaButton_Click;
            // 
            // childPanel
            // 
            childPanel.Location = new Point(367, 344);
            childPanel.Name = "childPanel";
            childPanel.Size = new Size(573, 179);
            childPanel.TabIndex = 20;
            // 
            // Form1
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(952, 612);
            Controls.Add(childPanel);
            Controls.Add(actualizeazaButton);
            Controls.Add(stergeButton);
            Controls.Add(adaugaButton);
            Controls.Add(childLabel);
            Controls.Add(viewChild);
            Controls.Add(parentLabel);
            Controls.Add(viewParent);
            Name = "Form1";
            Text = "Form1";
            Load += Form1_Load;
            ((System.ComponentModel.ISupportInitialize)viewParent).EndInit();
            ((System.ComponentModel.ISupportInitialize)viewChild).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion
        private DataGridView viewParent;
        private Label parentLabel;
        private DataGridView viewChild;
        private Label childLabel;
        private Button adaugaButton;
        private Button stergeButton;
        private Button actualizeazaButton;
        private Panel childPanel;
    }
}
