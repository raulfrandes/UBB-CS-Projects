using System;
using System.Data.SqlClient;
using System.Threading;

namespace Lab4
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            string connectionString = @"Server=DESKTOP-VLKB0QK\SQLEXPRESS;" +
                                      " Database=AplicatieCinematograf; Integrated Security=true;" +
                                      " TrustServerCertificate=true;";
            int retryCount = 0;
            bool success = false;

            while (!success && retryCount < 3)
            {
                Console.WriteLine("Retry count: " + retryCount);

                Thread thread1 = new Thread(() =>
                {
                    Console.WriteLine("Thread1 is running!");

                    using (SqlConnection connection = new SqlConnection(connectionString))
                    {
                        connection.Open();
                        using (SqlCommand setDeadlockPriorityCommand = connection.CreateCommand())
                        {
                            setDeadlockPriorityCommand.CommandText = "SET DEADLOCK_PRIORITY HIGH";
                            setDeadlockPriorityCommand.ExecuteNonQuery();
                        }

                        using (SqlTransaction transaction = connection.BeginTransaction())
                        {
                            try
                            {
                                using (SqlCommand command = connection.CreateCommand())
                                {
                                    command.Transaction = transaction;

                                    command.CommandText =
                                        "UPDATE Clienti SET nume='T1:Deadlock Clienti' WHERE dataNasterii='2003-05-19'";
                                    command.ExecuteNonQuery();

                                    Thread.Sleep(7000);

                                    command.CommandText =
                                        "UPDATE PreferinteClienti SET numeActorPreferat='T1:Deadlock Preferinte' WHERE numeDirectorPreferat='Preferinte Deadlock';";
                                    command.ExecuteNonQuery();
                                }

                                transaction.Commit();
                                Console.WriteLine("T1 committed successfully.");
                                success = true;
                            }
                            catch (SqlException ex)
                            {
                                if (ex.Number == 1205)
                                {
                                    Console.WriteLine("Deadlock occurred. Retrying...");

                                    transaction.Rollback();
                                    Console.WriteLine("T1 rolled back.");
                                    retryCount++;
                                }
                                else
                                {
                                    Console.WriteLine("Error occured: " + ex.Message);
                                    transaction.Rollback();
                                    Console.WriteLine("T1 rolled back.");
                                }
                            }
                        }
                    }
                });

                Thread thread2 = new Thread(() =>
                {
                    Console.WriteLine("Thread2 is running!");

                    using (SqlConnection connection = new SqlConnection(connectionString))
                    {
                        connection.Open();

                        using (SqlTransaction transaction = connection.BeginTransaction())
                        {
                            try
                            {
                                using (SqlCommand command = connection.CreateCommand())
                                {
                                    command.Transaction = transaction;

                                    command.CommandText =
                                        "UPDATE PreferinteClienti SET numeActorPreferat='T2:Deadlock Preferinte' WHERE numeDirectorPreferat='Preferinte Deadlock';";
                                    command.ExecuteNonQuery();

                                    Thread.Sleep(7000);

                                    command.CommandText =
                                        "UPDATE Clienti SET nume='T2:Deadlock Clienti' WHERE dataNasterii='2003-05-19'";
                                    command.ExecuteNonQuery();
                                }

                                transaction.Commit();
                                Console.WriteLine("T2 committed successfully.");
                                success = true;
                            }
                            catch (SqlException ex)
                            {
                                if (ex.Number == 1205)
                                {
                                    Console.WriteLine("Deadlock occurred. Retrying...");

                                    transaction.Rollback();
                                    Console.WriteLine("T2 rolled back.");
                                    retryCount++;
                                }
                                else
                                {
                                    Console.WriteLine("Error occured: " + ex.Message);
                                    transaction.Rollback();
                                    Console.WriteLine("T2 rolled back.");
                                }
                            }
                        }
                    }
                });
                
                thread1.Start();
                thread2.Start();
                thread1.Join();
                thread2.Join();
            }

            if (retryCount >= 3)
            {
                Console.WriteLine("Exceeded maximum retry attempts. Aborting.");
            }
            else
            {
                Console.WriteLine("All transactions completed.");
            }
        }
    }
}