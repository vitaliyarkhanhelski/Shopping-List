package com.example.wolskazakupy.controller;

import com.example.wolskazakupy.model.Zakup;
import com.example.wolskazakupy.service.ZakupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ZakupController {

    private ZakupService zakupService;

    @Autowired
    public ZakupController(ZakupService zakupService) {
        this.zakupService = zakupService;
    }


    @PostMapping("/save")
    public String save(@ModelAttribute Zakup zakup, @RequestParam(name = "file", required = false) MultipartFile file) {
//        System.out.println(zakup.getComment().isEmpty());
        Long initialId = zakup.getId();
        Optional<Zakup> myZakup = zakupService.findFirstByNameAndComment(zakup.getName(), zakup.getComment());
//        if (myZakup.isPresent() && !zakup.getComment().isEmpty()) {
        if (myZakup.isPresent()) {

//
//            Long a = zakup.getId();
//            Long b = myZakup.get().getId();
//            System.out.println(a);
//            System.out.println(b);
//            System.out.println(a.equals(b));

            if (zakup.getId() != null) {
                if (zakup.getId().equals(myZakup.get().getId())) {
                    System.out.println("Hello");
//                System.out.println("Hello");
                    if (file.getSize() != 0) {
                        zakupService.save(zakup, file);
                    } else {
                        zakupService.save(zakup);
                    }
                    return "redirect:/?updated=true";
                }
            }
            if (zakup.getComment().isEmpty() || myZakup.get().getComment().isEmpty()) {
                System.out.println("Case1");
                System.out.println(zakup.getComment());
                System.out.println(myZakup.get().getComment());
                if (file.getSize() != 0) {
                    zakupService.save(zakup, file);
                } else {
                    zakupService.save(zakup);
                }
                return "redirect:/?saved=true";
            }
            if (zakup.getName().equals(myZakup.get().getName()) && !zakup.getComment().equals(myZakup.get().getComment())) {
//                    System.out.println("Case2");
                if (file.getSize() != 0) {
                    zakupService.save(zakup, file);
                } else {
                    zakupService.save(zakup);
                }
                return "redirect:/?saved=true";
            }

//            System.out.println(zakup.getComment().isEmpty());
//            if (!zakup.getComment().isEmpty() || !myZakup.get().getComment().isEmpty()) {
//                System.out.println("Hello");
            return zakup.getId() != null ? "redirect:/?same=yes&zakupName=" + zakup.getName() + "&zakupComment=" + zakup.getComment()
                    + "&zakupInProgress=" + zakup.isInProcess() + "&update=true&zakupId=" + zakup.getId() + "&imageLink=" + zakup.getImgLink() :
                    "redirect:/?same=yes&zakupName=" + zakup.getName() + "&zakupComment=" + zakup.getComment() + "&zakupInProgress=" + zakup.isInProcess();
//            }
        } else if (file.getSize() != 0) {
            zakupService.save(zakup, file);
        } else {
            zakupService.save(zakup);
        }
        return initialId != null ? "redirect:/?updated=true" : "redirect:/?saved=true";
    }


    @PostMapping("/in-progress")
    public String inProgress(@RequestParam Long zakupId) {
        Zakup zakup = zakupService.findById(zakupId);
        if (zakup.isInProcess()) {
            zakup.setInProcess(false);
        } else {
            zakup.setInProcess(true);
        }
        zakupService.save(zakup);
        List<Zakup> list = getSortedZakupList();
        Zakup newZakup = zakupService.findById(zakupId);
        if (list.indexOf(newZakup) != 0) {
            return "redirect:/#place" + zakupId;
        } else {
            return "redirect:";
        }
    }


    @PostMapping("/find")
    public String find(@RequestParam String find, ModelMap map) {
        map.put("button", "Save");
        Zakup zakup = new Zakup();
        map.put("zakup", zakup);

        List<Zakup> list = new ArrayList<>();
        List<Zakup> allList = getSortedZakupList();
        if (allList.isEmpty()) {
            map.put("result", "No results found");
        } else {
            for (Zakup i : allList) {
                if (i.getName().toLowerCase().contains(find.toLowerCase())) {
                    list.add(i);
                }
            }
        }
        if (list.isEmpty()) {
            map.put("result", "No results found");
        } else {
            map.put("zakupy", list);
        }

        return "index";
    }


    @PostMapping("/delete")
    public String delete(@RequestParam Long zakupId) {
        List<Zakup> list = getSortedZakupList();
        Zakup zakup = zakupService.findById(zakupId);
        zakupService.delete(zakupId);
        int n = list.indexOf(zakup);
        if (n != 0) {
            return "redirect:/#place" + list.get(n - 1).getId();
        } else {
            return "redirect:";
        }
    }


    @GetMapping("/revert")
    public String revert(@RequestParam Long zakupId) {
        return "redirect:/update2?zakupId=" + zakupId;
    }


    @GetMapping("/deleteImage")
    public String deleteImage(@RequestParam Long id,
                              @RequestParam String name,
                              @RequestParam String comment,
                              @RequestParam boolean inProcess,
                              @RequestParam String imgLink,
                              ModelMap map) {
//        System.out.println(id);
//        System.out.println(name);
//        System.out.println(comment);
//        System.out.println(inProcess);
//        System.out.println(imgLink);
        Zakup zakup = new Zakup(id, name, comment, "", inProcess);
        zakupService.deleteImage(id);

        map.put("update", true);
        map.put("zakup", zakup);
        map.put("button", "Update");
        map.put("zakupy", getSortedZakupList());

        return "index";
    }


    @PostMapping("/update")
    public String update(@RequestParam Long zakupId, ModelMap map) {

        Zakup zakup = zakupService.findById(zakupId);
        if (!zakup.getImgLink().isEmpty()) {
            map.put("image", true);
        }
        map.put("update", true);
        map.put("zakup", zakup);
        map.put("button", "Update");
        map.put("zakupy", getSortedZakupList());
        return "index";
    }


    @GetMapping("/update2")
    public String update2(@RequestParam Long zakupId, ModelMap map) {
        Zakup zakup = zakupService.findById(zakupId);
        map.put("update", true);
        map.put("zakup", zakup);
        map.put("button", "Update");
        map.put("zakupy", getSortedZakupList());
        return "index";
    }


    @GetMapping("/")
    public String findAll(@RequestParam(required = false) String same,
                          @RequestParam(required = false) Long zakupId,
                          @RequestParam(required = false) String zakupName,
                          @RequestParam(required = false) String zakupComment,
                          @RequestParam(required = false) String imageLink,
                          @RequestParam(required = false) boolean zakupInProgress,
                          @RequestParam(required = false) boolean update,
                          @RequestParam(required = false) boolean updated,
                          @RequestParam(required = false) boolean saved,
                          ModelMap map) {
        Zakup zakup;
//        System.out.println(imageLink);
        if (same != null) {
            map.put("same", update ? "You can't update the item, the same item is already exist!" : "You can't save the item, the same item is already exist!");
            zakup = zakupId != null ? new Zakup(zakupId, zakupName, zakupComment, imageLink, zakupInProgress) :
                    new Zakup(zakupName, zakupComment, zakupInProgress);
        } else {
            zakup = new Zakup();
        }
        map.put("zakup", zakup);

        if (update) {
            map.put("button", "Update");
            map.put("update", true);
            if (!imageLink.isEmpty()) {
                map.put("image", true);
            }
        } else {
            map.put("button", "Save");
            if (updated) map.put("same", "Item was successfully updated");
            if (saved) map.put("same", "Item was successfully saved");
        }

        if (zakupService.findAll().size() != 0) {
            map.put("zakupy", getSortedZakupList());
        } else {
            map.put("result", "Stocks are replenished. Nothing is needed");
        }
        return "index";
    }


    public List<Zakup> getSortedZakupList() {
        return zakupService.findAll().stream().sorted(new Comparator<Zakup>() {
            @Override
            public int compare(Zakup o1, Zakup o2) {
                return (int) (o1.getId() - o2.getId());
            }
        }).collect(Collectors.toList());
    }

}
