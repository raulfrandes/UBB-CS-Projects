$param1 = $args[0] # Nume fisier java
#Write-Host $param1

$param2 = $args[1] # Nume metoda
#Write-Host $param2

$param3 = $args[2] # No of threads
#Write-Host $param2

$param4 = $args[3] # No of reading threads
#Write-Host $param2

$param5 = $args[4] # Tip runs

# Executare class Java

$suma = 0

for ($i = 0; $i -lt $param5; $i++){
    Write-Host "Rulare" ($i+1)
    $a = java $param1 $param2 $param3 $param4 # rulare class java
    Write-Host $a[$a.length-2]
    Write-Host $a[$a.length-1]
    $suma += $a[$a.length-2]
    Write-Host ""
}
$media = $suma / $i
#Write-Host $suma
Write-Host "Timp de executie mediu:" $media

# Creare fisier .csv
if (!(Test-Path outJ.csv)){
    New-Item outJ.csv -ItemType File
    #Scrie date in csv
    Set-Content outJ.csv 'Metoda,Nr threads,Nr reading threads,Timp executie'
}
#
# Append
Add-Content outJ.csv "$($param2),$($param3),$($param4),$($media)"