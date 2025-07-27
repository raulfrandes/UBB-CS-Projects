using Microsoft.Data.SqlClient;
using Microsoft.IdentityModel.Tokens;
using System.Data;

namespace CinemaApp
{
    public partial class Form1 : Form
    {
        string connectionString = @"Server = DESKTOP-VLKB0QK\SQLEXPRESS;Database=AplicatieCinematograf;Integrated Security=true;TrustServerCertificate=true";

        SqlDataAdapter daCategorii = new SqlDataAdapter();
        SqlDataAdapter daFilme = new SqlDataAdapter();
        DataSet ds = new DataSet();
        BindingSource bsCategorii;
        BindingSource bsFilme;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    daCategorii.SelectCommand = new SqlCommand("SELECT * FROM CategoriiFilme;", connection);
                    daFilme.SelectCommand = new SqlCommand("SELECT * FROM Filme;", connection);

                    ds.Clear();
                    daCategorii.Fill(ds, "CategoriiFilme");
                    daFilme.Fill(ds, "Filme");

                    DataColumn categoriiColumn = ds.Tables["CategoriiFilme"].Columns["catId"];
                    DataColumn filmeColumn = ds.Tables["Filme"].Columns["catId"];

                    DataRelation relation = new DataRelation("FK_CategoriiFilme_Filme", categoriiColumn, filmeColumn);
                    ds.Relations.Add(relation);

                    bsCategorii = new BindingSource();
                    bsCategorii.DataSource = ds.Tables["CategoriiFilme"];
                    viewCategorii.DataSource = bsCategorii;

                    bsFilme = new BindingSource();
                    bsFilme.DataSource = bsCategorii;
                    bsFilme.DataMember = "FK_CategoriiFilme_Filme";
                    viewFilme.DataSource = bsFilme;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void stergeButton_Click(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    int x = 0;
                    daFilme.DeleteCommand = new SqlCommand("DELETE FROM Filme WHERE fId=@id", connection);
                    daFilme.DeleteCommand.Parameters.Add("@id", SqlDbType.Int).Value = viewFilme.SelectedRows[0].Cells["fId"].FormattedValue.ToString();

                    connection.Open();
                    x = daFilme.DeleteCommand.ExecuteNonQuery();
                    if (x > 0)
                    {
                        ds.Clear();
                        daCategorii.SelectCommand = new SqlCommand("SELECT * FROM CategoriiFilme;", connection);
                        daFilme.SelectCommand = new SqlCommand("SELECT * FROM Filme;", connection);
                        daCategorii.Fill(ds, "CategoriiFilme");
                        daFilme.Fill(ds, "Filme");
                        MessageBox.Show("Film sters cu succes.");
                    }
                    else
                    {
                        MessageBox.Show("Stergerea a esuat!");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void viewFilme_SelectionChanged(object sender, EventArgs e)
        {
            if (viewFilme.SelectedRows.Count > 0)
            {
                DataGridViewRow selectedRow = viewFilme.SelectedRows[0];

                txtTitlu.Text = selectedRow.Cells["titlu"].Value.ToString();
                txtDirector.Text = selectedRow.Cells["numeDirector"].Value.ToString();
                txtDurata.Text = selectedRow.Cells["durata"].Value.ToString();
                txtDescriere.Text = selectedRow.Cells["descriere"].Value.ToString();
                txtPret.Text = selectedRow.Cells["pret"].Value.ToString();
                txtCategorie.Text = selectedRow.Cells["catId"].Value.ToString();
            }
            else
            {
                txtTitlu.Text = "";
                txtDirector.Text = "";
                txtDurata.Text = "";
                txtDescriere.Text = "";
                txtPret.Text = "";
                txtCategorie.Text = "";
            }
        }

        private void actualizeazaButton_Click(object sender, EventArgs e)
        {
            try
            {
                String titlu = txtTitlu.Text;
                String director = txtDirector.Text;
                int durata = int.Parse(txtDurata.Text);
                String descriere = txtDescriere.Text;
                int pret = int.Parse(txtPret.Text);

                if (titlu.IsNullOrEmpty())
                {
                    throw new Exception("Titlul nu poate fi null!");
                }
                if (director.IsNullOrEmpty())
                {
                    throw new Exception("Directorul nu poagte fi null!");
                }
                if (durata < 0)
                {
                    throw new Exception("Durata nu poate fi negativa!");
                }
                if (descriere.IsNullOrEmpty())
                {
                    throw new Exception("Descrierea nu poate fi null!");
                }
                if (pret < 0)
                {
                    throw new Exception("Pretul nu poate fi negativ!");
                }

                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    int x = 0;
                    daFilme.UpdateCommand = new SqlCommand("UPDATE Filme SET titlu=@titlu, numeDirector=@director, durata=@durata, descriere=@descriere, pret=@pret, catId=@categorie WHERE fId=@id;", connection);

                    daFilme.UpdateCommand.Parameters.Add("@titlu", SqlDbType.VarChar).Value = txtTitlu.Text;
                    daFilme.UpdateCommand.Parameters.Add("@director", SqlDbType.VarChar).Value = txtDirector.Text;
                    daFilme.UpdateCommand.Parameters.Add("@durata", SqlDbType.Int).Value = txtDurata.Text;
                    daFilme.UpdateCommand.Parameters.Add("@descriere", SqlDbType.VarChar).Value = txtDescriere.Text;
                    daFilme.UpdateCommand.Parameters.Add("@pret", SqlDbType.Int).Value = txtPret.Text;
                    daFilme.UpdateCommand.Parameters.Add("@categorie", SqlDbType.Int).Value = txtCategorie.Text;
                    daFilme.UpdateCommand.Parameters.Add("@id", SqlDbType.Int).Value = viewFilme.SelectedRows[0].Cells["fId"].FormattedValue.ToString();

                    connection.Open();
                    x = daFilme.UpdateCommand.ExecuteNonQuery();
                    if (x > 0)
                    {
                        ds.Clear();
                        daCategorii.SelectCommand = new SqlCommand("SELECT * FROM CategoriiFilme;", connection);
                        daFilme.SelectCommand = new SqlCommand("SELECT * FROM Filme;", connection);
                        daCategorii.Fill(ds, "CategoriiFilme");
                        daFilme.Fill(ds, "Filme");
                        MessageBox.Show("Film actualizat cu succes.");
                    }
                    else
                    {
                        MessageBox.Show("Actualizarea a esuat!");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void adaugaButton_Click(object sender, EventArgs e)
        {
            try
            {
                String titlu = txtTitlu.Text;
                String director = txtDirector.Text;
                int durata = int.Parse(txtDurata.Text);
                String descriere = txtDescriere.Text;
                int pret = int.Parse(txtPret.Text);
                Int32 catId = Int32.Parse(txtCategorie.Text);

                if (titlu.IsNullOrEmpty())
                {
                    throw new Exception("Titlul nu poate fi null!");
                }
                if (director.IsNullOrEmpty())
                {
                    throw new Exception("Directorul nu poagte fi null!");
                }
                if (durata < 0)
                {
                    throw new Exception("Durata nu poate fi negativa!");
                }
                if (descriere.IsNullOrEmpty())
                {
                    throw new Exception("Descrierea nu poate fi null!");
                }
                if (pret < 0)
                {
                    throw new Exception("Pretul nu poate fi negativ!");
                }

                bool exists = false;
                foreach (DataGridViewRow row in viewCategorii.Rows)
                {
                    if (catId.Equals(row.Cells[0].Value))
                    {
                        exists = true; break;
                    }
                }
                if (!exists)
                {
                    throw new Exception("Categoria nu exista!");
                }

                using(SqlConnection connection = new SqlConnection(connectionString))
                {
                    int x = 0;
                    daFilme.InsertCommand = new SqlCommand("INSERT INTO Filme (titlu, numeDirector, durata, descriere, pret, catId) VALUES (@titlu, @director, @durata, @descriere, @pret, @catId);", connection);
                    daFilme.InsertCommand.Parameters.Add("@titlu", SqlDbType.VarChar).Value = txtTitlu.Text;
                    daFilme.InsertCommand.Parameters.Add("@director", SqlDbType.VarChar).Value = txtDirector.Text;
                    daFilme.InsertCommand.Parameters.Add("@durata", SqlDbType.Int).Value = txtDurata.Text;
                    daFilme.InsertCommand.Parameters.Add("@descriere", SqlDbType.VarChar).Value = txtDescriere.Text;
                    daFilme.InsertCommand.Parameters.Add("@pret", SqlDbType.Int).Value = txtPret.Text;
                    daFilme.InsertCommand.Parameters.Add("@catId", SqlDbType.Int).Value = txtCategorie.Text;

                    connection.Open();
                    x = daFilme.InsertCommand.ExecuteNonQuery();
                    if (x > 0)
                    {
                        ds.Clear();
                        daCategorii.SelectCommand = new SqlCommand("SELECT * FROM CategoriiFilme;", connection);
                        daFilme.SelectCommand = new SqlCommand("SELECT * FROM Filme;", connection);
                        daCategorii.Fill(ds, "CategoriiFilme");
                        daFilme.Fill(ds, "Filme");
                        MessageBox.Show("Film adaugat cu succes.");
                    }
                    else
                    {
                        MessageBox.Show("Adaugarea a esuat!");
                    }
                }
            } catch(Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
    }
}
