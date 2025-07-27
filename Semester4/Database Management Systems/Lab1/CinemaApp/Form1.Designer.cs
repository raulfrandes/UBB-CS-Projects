namespace CinemaApp
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
            viewCategorii = new DataGridView();
            label1 = new Label();
            viewFilme = new DataGridView();
            label2 = new Label();
            txtTitlu = new TextBox();
            txtDirector = new TextBox();
            txtDurata = new TextBox();
            txtDescriere = new TextBox();
            txtPret = new TextBox();
            txtCategorie = new TextBox();
            adaugaButton = new Button();
            stergeButton = new Button();
            label3 = new Label();
            label4 = new Label();
            label5 = new Label();
            label6 = new Label();
            label7 = new Label();
            label8 = new Label();
            actualizeazaButton = new Button();
            ((System.ComponentModel.ISupportInitialize)viewCategorii).BeginInit();
            ((System.ComponentModel.ISupportInitialize)viewFilme).BeginInit();
            SuspendLayout();
            // 
            // viewCategorii
            // 
            viewCategorii.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            viewCategorii.Location = new Point(12, 344);
            viewCategorii.Name = "viewCategorii";
            viewCategorii.RowHeadersWidth = 51;
            viewCategorii.Size = new Size(325, 257);
            viewCategorii.TabIndex = 1;
            // 
            // label1
            // 
            label1.AutoSize = true;
            label1.Font = new Font("Arial", 10.8F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label1.Location = new Point(12, 320);
            label1.Name = "label1";
            label1.Size = new Size(82, 21);
            label1.TabIndex = 2;
            label1.Text = "Categorii";
            // 
            // viewFilme
            // 
            viewFilme.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            viewFilme.Location = new Point(12, 38);
            viewFilme.Name = "viewFilme";
            viewFilme.RowHeadersWidth = 51;
            viewFilme.Size = new Size(928, 257);
            viewFilme.TabIndex = 3;
            viewFilme.SelectionChanged += viewFilme_SelectionChanged;
            // 
            // label2
            // 
            label2.AutoSize = true;
            label2.Font = new Font("Arial", 10.8F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label2.Location = new Point(12, 14);
            label2.Name = "label2";
            label2.Size = new Size(53, 21);
            label2.TabIndex = 4;
            label2.Text = "Filme";
            // 
            // txtTitlu
            // 
            txtTitlu.Location = new Point(367, 366);
            txtTitlu.Name = "txtTitlu";
            txtTitlu.Size = new Size(150, 27);
            txtTitlu.TabIndex = 5;
            // 
            // txtDirector
            // 
            txtDirector.Location = new Point(578, 366);
            txtDirector.Name = "txtDirector";
            txtDirector.Size = new Size(150, 27);
            txtDirector.TabIndex = 6;
            // 
            // txtDurata
            // 
            txtDurata.Location = new Point(790, 366);
            txtDurata.Name = "txtDurata";
            txtDurata.Size = new Size(150, 27);
            txtDurata.TabIndex = 7;
            // 
            // txtDescriere
            // 
            txtDescriere.Location = new Point(367, 450);
            txtDescriere.Name = "txtDescriere";
            txtDescriere.Size = new Size(150, 27);
            txtDescriere.TabIndex = 8;
            // 
            // txtPret
            // 
            txtPret.Location = new Point(578, 450);
            txtPret.Name = "txtPret";
            txtPret.Size = new Size(150, 27);
            txtPret.TabIndex = 9;
            // 
            // txtCategorie
            // 
            txtCategorie.Location = new Point(790, 450);
            txtCategorie.Name = "txtCategorie";
            txtCategorie.Size = new Size(150, 27);
            txtCategorie.TabIndex = 10;
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
            // label3
            // 
            label3.AutoSize = true;
            label3.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label3.Location = new Point(367, 344);
            label3.Name = "label3";
            label3.Size = new Size(37, 19);
            label3.TabIndex = 13;
            label3.Text = "Titlu";
            // 
            // label4
            // 
            label4.AutoSize = true;
            label4.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label4.Location = new Point(578, 344);
            label4.Name = "label4";
            label4.Size = new Size(115, 19);
            label4.TabIndex = 14;
            label4.Text = "Nume Director";
            // 
            // label5
            // 
            label5.AutoSize = true;
            label5.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label5.Location = new Point(790, 344);
            label5.Name = "label5";
            label5.Size = new Size(58, 19);
            label5.TabIndex = 15;
            label5.Text = "Durata";
            // 
            // label6
            // 
            label6.AutoSize = true;
            label6.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label6.Location = new Point(580, 428);
            label6.Name = "label6";
            label6.Size = new Size(39, 19);
            label6.TabIndex = 16;
            label6.Text = "Pret";
            // 
            // label7
            // 
            label7.AutoSize = true;
            label7.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label7.Location = new Point(367, 428);
            label7.Name = "label7";
            label7.Size = new Size(81, 19);
            label7.TabIndex = 17;
            label7.Text = "Descriere";
            // 
            // label8
            // 
            label8.AutoSize = true;
            label8.Font = new Font("Arial", 10.2F, FontStyle.Regular, GraphicsUnit.Point, 0);
            label8.Location = new Point(790, 428);
            label8.Name = "label8";
            label8.Size = new Size(99, 19);
            label8.TabIndex = 18;
            label8.Text = "Id Categorie";
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
            // Form1
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(952, 612);
            Controls.Add(actualizeazaButton);
            Controls.Add(label8);
            Controls.Add(label7);
            Controls.Add(label6);
            Controls.Add(label5);
            Controls.Add(label4);
            Controls.Add(label3);
            Controls.Add(stergeButton);
            Controls.Add(adaugaButton);
            Controls.Add(txtCategorie);
            Controls.Add(txtPret);
            Controls.Add(txtDescriere);
            Controls.Add(txtDurata);
            Controls.Add(txtDirector);
            Controls.Add(txtTitlu);
            Controls.Add(label2);
            Controls.Add(viewFilme);
            Controls.Add(label1);
            Controls.Add(viewCategorii);
            Name = "Form1";
            Text = "Form1";
            Load += Form1_Load;
            ((System.ComponentModel.ISupportInitialize)viewCategorii).EndInit();
            ((System.ComponentModel.ISupportInitialize)viewFilme).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion
        private DataGridView viewCategorii;
        private Label label1;
        private DataGridView viewFilme;
        private Label label2;
        private TextBox txtTitlu;
        private TextBox txtDirector;
        private TextBox txtDurata;
        private TextBox txtDescriere;
        private TextBox txtPret;
        private TextBox txtCategorie;
        private Button adaugaButton;
        private Button stergeButton;
        private Label label3;
        private Label label4;
        private Label label5;
        private Label label6;
        private Label label7;
        private Label label8;
        private Button actualizeazaButton;
    }
}
