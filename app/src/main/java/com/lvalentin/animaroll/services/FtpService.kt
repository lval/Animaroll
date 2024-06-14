//package com.lvalentin.animaroll.services
//
//import org.apache.commons.net.ftp.FTP
//import org.apache.commons.net.ftp.FTPClient
//import org.apache.commons.net.ftp.FTPFile
//
//data class AlbumItem(val name: String, val artistNames: List<String>, val imageUrl: String)
//
//class FtpService(private val hostname: String, private val username: String, private val password: String) {
//
//    private val ftpClient: FTPClient = FTPClient()
//    private val imageExtensions = listOf(".jpg", ".jpeg", ".png", ".bmp")
//
//    init {
//        val ftpServer = "192.168.86.227"
//        val port = 9999
//        ftpClient.connect(ftpServer, port)
//    }
//
//    fun login() {
//        val username1 = "anonymous"
//        val password1 = ""
//        ftpClient.login(username1, password1)
//        ftpClient.enterLocalPassiveMode()
//        ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
//    }
//
//    fun logout() {
//        ftpClient.logout()
//    }
//
//    fun disconnect() {
//        try {
//            ftpClient.disconnect()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun getFoldersWithImages(remoteBaseDirectory: String): List<String> {
//
////        val remoteRootDirectory = "/"
////        val fileNames = ftpClient.listNames(remoteRootDirectory)
////
////        if (fileNames != null) {
////            for (fileName in fileNames) {
////                val extension = getFileExtension(fileName)
////                if (extension in imageExtensions) {
////                    val folderPath = getFolderPath(remoteRootDirectory, fileName)
////                    if (!foldersWithImages.contains(folderPath)) {
////                        foldersWithImages.add(folderPath)
////                    }
////                }
////            }
////        }
//
//        val foldersWithImages = mutableListOf<String>()
//
//        try {
//            if (ftpClient.changeWorkingDirectory(remoteBaseDirectory)) {
//                val ftpFiles: Array<FTPFile> = ftpClient.listFiles()
//                for (ftpFile in ftpFiles) {
//                    if (ftpFile.isDirectory && containsImageFiles(remoteBaseDirectory + "/" + ftpFile.name)) {
//                        foldersWithImages.add(ftpFile.name)
//                    }
//                }
//            } else {
//                println("Remote directory not found.")
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return foldersWithImages
//    }
//
//    private fun containsImageFiles(remoteDirectory: String): Boolean {
//        ftpClient.changeWorkingDirectory(remoteDirectory)
//
//
////        for (fileName in fileNames) {
////            val extension = getFileExtension(fileName)
////            if (extension in imageExtensions) {
////                val folderPath = getFolderPath(remoteRootDirectory, fileName)
////                if (!foldersWithImages.contains(folderPath)) {
////                    foldersWithImages.add(folderPath)
////                }
////            }
////        }
//
//
//        val ftpFiles: Array<FTPFile> = ftpClient.listFiles()
//        for (ftpFile in ftpFiles) {
//            if (ftpFile.isFile && ftpFile.name.endsWith(".jpg", true)) {
//                return true
//            }
//        }
//        return false
//    }
//}
