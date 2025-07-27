using Microsoft.Data.SqlClient;
using Microsoft.IdentityModel.Tokens;
using System.Configuration;
using System.Data;

namespace CinemaAppGeneric
{
    public partial class Form1 : Form
    {
        string connectionString = ConfigurationManager.ConnectionStrings["connection"].ConnectionString;

        SqlDataAdapter daParent = new SqlDataAdapter();
        SqlDataAdapter daChild = new SqlDataAdapter();
        DataSet ds = new DataSet();
        BindingSource bsParent;
        BindingSource bsChild;
        Dictionary<string, TextBox> textBoxes;

        public Form1()
        {
            InitializeComponent();
            textBoxes = new Dictionary<string, TextBox>();
        }

        private void AddTextBoxes()
        {
            List<string> childColumnNames = ConfigurationManager.AppSettings["childColumnNames"].Split(",").ToList();
            int x = 0;
            int y = 40;
            int i = 0;
            foreach (string childColumn in childColumnNames)
            {
                TextBox textBox = new TextBox();
                Label label = new Label();
                textBox.Location = new System.Drawing.Point(x, y);
                label.Location = new System.Drawing.Point(x, y - 20);
                textBox.Size = new System.Drawing.Size(171, 30);
                label.Size = new System.Drawing.Size(171, 20);
                label.Text = childColumn;

                childPanel.Controls.Add(textBox);
                childPanel.Controls.Add(label);
                textBoxes[childColumn] = textBox;

                if ((i + 1) % 3 == 0)
                {
                    x = 0;
                    y += 80;
                }
                else
                {
                    x += 201;
                }
                i++;
            }
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                AddTextBoxes();
                string parentTable = ConfigurationManager.AppSettings["parentTable"];
                parentLabel.Text = parentTable;
                string childTable = ConfigurationManager.AppSettings["childTable"];
                childLabel.Text = childTable;
                string parentPrimaryKey = ConfigurationManager.AppSettings["parentPrimaryKey"];
                string childForeignKey = ConfigurationManager.AppSettings["childForeignKey"];
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    daParent.SelectCommand = new SqlCommand("SELECT * FROM " + parentTable + ";", connection);
                    daChild.SelectCommand = new SqlCommand("SELECT * FROM " + childTable + ";", connection);

                    ds.Clear();
                    daParent.Fill(ds, parentTable);
                    daChild.Fill(ds, childTable);

                    DataColumn parentColumn = ds.Tables[parentTable].Columns[parentPrimaryKey];
                    DataColumn childColumn = ds.Tables[childTable].Columns[childForeignKey];

                    DataRelation relation = new DataRelation("FK_CategoriiFilme_Filme", parentColumn, childColumn);
                    ds.Relations.Add(relation);

                    bsParent = new BindingSource();
                    bsParent.DataSource = ds.Tables[parentTable];
                    viewParent.DataSource = bsParent;

                    bsChild = new BindingSource();
                    bsChild.DataSource = bsParent;
                    bsChild.DataMember = "FK_CategoriiFilme_Filme";
                    viewChild.DataSource = bsChild;
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
                string parentTable = ConfigurationManager.AppSettings["parentTable"];
                string childTable = ConfigurationManager.AppSettings["childTable"];
                string childPrimaryKey = ConfigurationManager.AppSettings["childPrimaryKey"];
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    int x = 0;
                    daChild.DeleteCommand = new SqlCommand("DELETE FROM " + childTable + " WHERE " + childPrimaryKey + "=@id;", connection);
                    daChild.DeleteCommand.Parameters.AddWithValue("@id", viewChild.SelectedRows[0].Cells[childPrimaryKey].FormattedValue.ToString());

                    connection.Open();
                    x = daChild.DeleteCommand.ExecuteNonQuery();
                    if (x > 0)
                    {
                        ds.Clear();
                        daParent.SelectCommand = new SqlCommand("SELECT * FROM " + parentTable + ";", connection);
                        daChild.SelectCommand = new SqlCommand("SELECT * FROM " + childTable + ";", connection);
                        daParent.Fill(ds, parentTable);
                        daChild.Fill(ds, childTable);
                        MessageBox.Show("Inregistrarea s-a sters cu succes.");
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

        private void viewChild_SelectionChanged(object sender, EventArgs e)
        {
            List<string> childColumnNames = ConfigurationManager.AppSettings["childColumnNames"].Split(",").ToList();
            if (viewChild.SelectedRows.Count > 0)
            {
                DataGridViewRow selectedRow = viewChild.SelectedRows[0];

                foreach (var column in childColumnNames){
                    textBoxes[column].Text = selectedRow.Cells[column].Value.ToString();
                }
            }
            else
            {
                foreach (var column in childColumnNames)
                {
                    textBoxes[column].Clear();
                }
            }
        }

        private void actualizeazaButton_Click(object sender, EventArgs e)
        {
            try
            {
                List<string> childColumnNames = ConfigurationManager.AppSettings["childColumnNames"].Split(",").ToList();
                foreach (var column in childColumnNames)
                {
                    String field = textBoxes[column].Text;
                    if (field.IsNullOrEmpty())
                    {
                        throw new Exception(column + " nu poate fi null!");
                    }
                    if (int.TryParse(field, out int result))
                    {
                        if (result < 0)
                        {
                            throw new Exception(column + " nu poate avea valori negative");
                        }
                    }
                }

                string parentTable = ConfigurationManager.AppSettings["parentTable"];
                string childTable = ConfigurationManager.AppSettings["childTable"];
                string childPrimaryKey = ConfigurationManager.AppSettings["childPrimaryKey"];
                List<string> childColumnParameters = ConfigurationManager.AppSettings["childColumnParameters"].Split(",").ToList();
                string updateCommand = "UPDATE " + childTable + " SET ";
                for (int i = 0; i <  childColumnParameters.Count; i++)
                {
                    updateCommand += childColumnNames[i] + "=" + childColumnParameters[i];
                    if (i != childColumnParameters.Count - 1)
                    {
                        updateCommand += ", ";
                    }
                }
                updateCommand += " WHERE " + childPrimaryKey + "=@id;";
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    int x = 0;
                    daChild.UpdateCommand = new SqlCommand(updateCommand, connection);

                    for (int i = 0; i < childColumnParameters.Count; i++)
                    {
                        daChild.UpdateCommand.Parameters.AddWithValue(childColumnParameters[i], textBoxes[childColumnNames[i]].Text.ToString());
                    }
                    daChild.UpdateCommand.Parameters.AddWithValue("@id", viewChild.SelectedRows[0].Cells[childPrimaryKey].FormattedValue.ToString());

                    connection.Open();
                    x = daChild.UpdateCommand.ExecuteNonQuery();
                    if (x > 0)
                    {
                        ds.Clear();
                        daParent.SelectCommand = new SqlCommand("SELECT * FROM " + parentTable + ";", connection);
                        daChild.SelectCommand = new SqlCommand("SELECT * FROM " + childTable + ";", connection);
                        daParent.Fill(ds, parentTable);
                        daChild.Fill(ds, childTable);
                        MessageBox.Show("Inregistrarea s-a actualizat cu succes.");
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
                string childColumnNames = ConfigurationManager.AppSettings["childColumnNames"];
                List<string> childColumnNamesList = childColumnNames.Split(",").ToList();
                foreach (var column in childColumnNamesList)
                {
                    String field = textBoxes[column].Text;
                    if (field.IsNullOrEmpty())
                    {
                        throw new Exception(column + " nu poate fi null!");
                    }
                    if (int.TryParse(field, out int result))
                    {
                        if (result < 0)
                        {
                            throw new Exception(column + " nu poate avea valori negative");
                        }
                    }
                }

                string parentTable = ConfigurationManager.AppSettings["parentTable"];
                string childTable = ConfigurationManager.AppSettings["childTable"];
                string childColumnParameters = ConfigurationManager.AppSettings["childColumnParameters"];
                List<string> childColumnParametersList = childColumnParameters.Split(",").ToList();
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    int x = 0;
                    daChild.InsertCommand = new SqlCommand("INSERT INTO " + childTable + " (" + childColumnNames + ") VALUES (" + childColumnParameters + ");", connection);
                    for (int i = 0; i < childColumnParametersList.Count; i++)
                    {
                        daChild.InsertCommand.Parameters.AddWithValue(childColumnParametersList[i], textBoxes[childColumnNamesList[i]].Text);
                    }

                    connection.Open();
                    x = daChild.InsertCommand.ExecuteNonQuery();
                    if (x > 0)
                    {
                        ds.Clear();
                        daParent.SelectCommand = new SqlCommand("SELECT * FROM " + parentTable + ";", connection);
                        daChild.SelectCommand = new SqlCommand("SELECT * FROM " + childTable + ";", connection);
                        daParent.Fill(ds, parentTable);
                        daChild.Fill(ds, childTable);
                        MessageBox.Show("Inregistrarea s-a adaugat cu succes.");
                    }
                    else
                    {
                        MessageBox.Show("Adaugarea a esuat!");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
    }
}
