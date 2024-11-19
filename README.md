
**WEEK  10/31**

**Update on MongoDB Setup and Document Handling**(denis)

Since Kendra couldn’t attend class and Hunter arrived a bit late, I wanted to share what we learned about using MongoDB for project. I've been able to gather all the information,so I created a MongoDB database to store our documents.

### What We Did

1. **MongoDB Setup:** We learned how to create and manage a MongoDB database. I have already set up our database to facilitate document uploads.
   
2. **Project Structure:** I created a project tree using the UML diagram that we submit to the instractor. You can take a look at the code, which illustrates how to use MongoDB to create and upload documents.

### Current Challenge

One issue we’re facing before actulaly go deeper into the project  is to upload documents to MongoDB , We dpnt have a method to extract the contents of those documents. For our previous homework assignments (Homework 2 and 3), we did methods to extract text from documents, and I’d like to use similar logic for our team project.

### Request for Assistance

I need your help to provide code that will read and extract every word from a document just this and then we will be able to use the DAtaManager method to upload document to MangoDB. While I can utilize my code from past assignments, it’s important to ensure that we collaborate on this as a team project. Please share your ideas or code  so we can push it into our Github project.


ps: in the datamanager.java we will call the method that is going to extarxt the document so, I call it extractDocument, so when youll work on it just name the file like that .

Thank you!

Here is a Screenshot of what our tree project look like in the computer(We also discuss about it during class with the instructor) .

![2024-10-31](https://github.com/user-attachments/assets/58dad2c9-7cb3-44e0-a56a-963bf819a6a7)


## Week 11/11: Document Processing and Upload(denis)


### Objectives
1. Implement the `ExtractDocument` class to read a document, extract its text, and calculate word frequencies.
2. Integrate the `ExtractDocument` class with `DatabaseManager` to automatically extract and upload word frequency data.
3. Lay the groundwork for similarity search functionality by preparing documents for analysis.

### Progress
- **ExtractDocument Class**: Developed a method to read a document file, split it into individual words, and calculate word frequencies. This data is stored in a `Map<String, Integer>` format.
  - **Method**: `getWordFrequencies(filePath)` reads the document and returns a frequency map.
- **DatabaseManager **: Updated `DatabaseManager` to use the `ExtractDocument` class for extracting word frequencies and uploading them to MongoDB.
  - **New Method**: `uploadDocument(filePath)` now uses `ExtractDocument.getWordFrequencies()` to process the document before uploading it to MongoDB.

### Next Steps
- We will need to Implement the `SimilarityCalculator` class to compare word frequency using a similarity method .
- I want You gues to come up with a simple SimilarityCalculator code that will copmpare documents frequencies ad tell if they are similar or not , i will work on implement it to the project, if no solutio came up by 2 weeks I will do it myself then.

## result of the upload to MangoDB:

![2024-11-13](https://github.com/user-attachments/assets/44b87eb2-204b-462c-a8be-c3339211d4f6)



**New Additions 11/15**

Added Article Class and Updated DataManager. 
- Added article class which includes ways articles are indetified with a bunch of getters to get that information
- Updated DataManager.java, tried to implement the add article, list articles, while keeping the word frequency intact

  When run should create the CSV file with the article information. The article information  is hardcoded in the main located in the DataManager.java file.
  The program should save the file to the CSV while also updating MongoDB with any new articles.

  ## Week 11/18: Document Processing and Upload(denis)
We had to change some stuff in the codes to make it work with our Json data set file . Now we able to upload documents from the dataset to MangoDB.

Newspaper Class
Represents a newspaper document with fields such as name, website, cityCountyName, usState, and more.

Methods:
Newspaper(...): Constructor to initialize all fields.
getDocument(): Converts the Newspaper instance into a MongoDB-compatible Document.

Use the Database class to ingest a dataset into MongoDB. Documents should follow the structure of the Newspaper class.

### Next Steps
- We will need to Implement the class to compare word frequency using a similarity method .
- I want You gues to come up with a simple SimilarityCalculator code that will copmpare documents frequencies ad tell if they are similar or not , i will work on implement it to the project, if no solutio came up by 2 weeks I will do it myself then.

## result of the upload to MangoDB:

![2024-11-18](https://github.com/user-attachments/assets/e148495e-2169-4a8e-a5c0-12439ae7e427)

---



