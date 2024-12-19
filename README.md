# Chromatography Peak Picker
This project is a small java program that can help pick out peak centers and areas of chromatography .csv files. Provided a retention time library (more details below), it can also make an estimate of the identity of each peak.

## Getting Started
Begin my putting all chromatography csv files you want to analyze in a folder along with a copy of the compiled .jar. Then, open a command prompt in that folder and run the command `java -jar GC-Peak-Picker.jar <flags>` with any/none of the flags listed below. Be sure that the jar file name matches whatever you type in the command.

## Start-up Flags

Use **-t \<number\>** to set a threshold to search for peaks with intensity above \<number\>. If none is specified, an automatic baseline is determined based on the first 100 entries.

Use **-l \<csv library file name in working directory\>** to set the path to a tab-delimited csv retention time library for identification purposes.

Use **-e \<number\>** to set an allowed deviation of time for identification purposes if a retention time library was provided.

Use **-p \<path\>** to set a custom execution path to search for CSV files. The path MUST be in quotes.

## Example Start Commands
`java -jar GC-Peak-Picker.jar -t 30 -l library.csv -e 0.05` would find all peaks in all csv files in the folder that have intensity above a baseline of 30 and attempt to assign their identities using the provided library.csv file in the working directory with an identification tolerance of plus/minus 0.03.

`java -jar GC-Peak-Picker.jar -l library.csv -p "C:\Users\opd\OneDrive\Desktop\myData"` would find all peaks in all csv files in the folder names "myData" above a calculated baseline and attempt to identify them using the library.csv that's in the same directory as the jar file. The default allowed tolerance for identification is 0.03 if not specified using -e.

## Retention Time Libraries
You can provide a custom library of known retention times and compound identities if you've confirmed them via other means (mass spec etc.). The library file should be a tab-delimited .csv file with the identity on the left most column and the expected retention time being the right. The file should be placed in the same folder as the jar and data files being analyzed. An example library [can be found here.](https://github.com/opd02/Gas-Chromatography-Peak-Picker/blob/master/src/main/resources/ExampleLibrary.CSV)
